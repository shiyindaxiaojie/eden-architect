/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.spring.integration.ftpclient.core;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;
import org.ylzl.eden.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

/**
 * FTPClient 模板
 *
 * @author gyl
 * @since 2.0.0
 */
public class FTPClientTemplate {

	private static final String MSG_CHANGE_DIR_FAILED =
		"FTPClient changeWorkingDirectory to `{0}` failed";

	private static final String MSG_FILE_UNAVAILABLE = "FTPClient receive file `{0}` unavailable";

	private FTPClientPool pool;

	public FTPClientTemplate(FTPClientPool pool) {
		this.pool = pool;
	}

	public boolean makeDirectory(final String pathname) throws Exception {
		return new Template<Boolean>() {

			@Override
			Boolean run(FTPClient client) throws IOException {
				return client.makeDirectory(pathname);
			}
		}.exec();
	}

	public boolean removeDirectory(final String pathname) throws Exception {
		return new Template<Boolean>() {

			@Override
			Boolean run(FTPClient client) throws IOException {
				return client.removeDirectory(pathname);
			}
		}.exec();
	}

	public boolean deleteFile(final String pathname, final String remote) throws Exception {
		return new Template<Boolean>() {

			@Override
			Boolean run(FTPClient client) throws IOException {
				if (!client.changeWorkingDirectory(pathname)) {
					throw new RuntimeException(MessageFormat.format(MSG_CHANGE_DIR_FAILED, pathname));
				}

				return client.deleteFile(remote);
			}
		}.exec();
	}

	public boolean storeFile(final String pathname, final String remote, final InputStream local)
		throws Exception {
		return new Template<Boolean>() {

			@Override
			Boolean run(FTPClient client) throws IOException {
				if (!client.changeWorkingDirectory(pathname)) {
					throw new RuntimeException(MessageFormat.format(MSG_CHANGE_DIR_FAILED, pathname));
				}

				try {
					return client.storeFile(remote, local);
				} finally {
					client.completePendingCommand();
				}
			}
		}.exec();
	}

	public byte[] retrieveFileStream(final String pathname, final String remote) throws Exception {
		return new Template<byte[]>() {

			@Override
			byte[] run(FTPClient client) throws IOException {
				if (!client.changeWorkingDirectory(pathname)) {
					throw new RuntimeException(MessageFormat.format(MSG_CHANGE_DIR_FAILED, pathname));
				}

				try (InputStream in = client.retrieveFileStream(remote);) {
					if (in == null || client.getReplyCode() == FTPReply.FILE_UNAVAILABLE) {
						throw new RuntimeException(
							MessageFormat.format(MSG_FILE_UNAVAILABLE, client.getReplyCode()));
					}

					return IOUtils.toByteArray(in);
				} finally {
					client.completePendingCommand();
				}
			}
		}.exec();
	}

	public boolean isExists(final String pathname, final String remote) throws Exception {
		return new Template<Boolean>() {

			@Override
			Boolean run(FTPClient client) throws IOException {
				return client.listFiles(
					pathname,
					new FTPFileFilter() {

						@Override
						public boolean accept(FTPFile ftpFile) {
							if (!ftpFile.isFile()) {
								return false;
							}
							return ftpFile.getName().equals(remote);
						}
					})
					.length
					> 0;
			}
		}.exec();
	}

	private abstract class Template<T> {

		public T exec() throws Exception {
			FTPClient client = null;
			try {
				client = get();
				return run(client);
			} finally {
				close(client);
			}
		}

		private FTPClient get() throws Exception {
			return pool.borrowObject();
		}

		private void close(FTPClient client) {
			if (pool != null) {
				pool.returnObject(client);
			}
		}

		abstract T run(FTPClient client) throws IOException;
	}
}
