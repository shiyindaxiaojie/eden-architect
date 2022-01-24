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
package org.ylzl.eden.spring.integration.truelicense.manager;

import de.schlichtherle.license.*;
import de.schlichtherle.xml.GenericCertificate;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.commons.env.CharsetConstants;
import org.ylzl.eden.commons.env.OSUtils;
import org.ylzl.eden.commons.lang.ObjectUtils;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.spring.integration.truelicense.env.TrueLicenseProperties;
import org.ylzl.eden.spring.integration.truelicense.keystore.CustomKeyStoreParam;
import org.ylzl.eden.spring.integration.truelicense.manager.env.LicenseOS;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * 增强式许可证管理
 *
 * @author gyl
 * @since 2.4.x
 */
@Slf4j
public final class EnhancedLicenseManager extends LicenseManager {

	private static final int DEFAULT_BUFSIZE = 8 * 1024;

	private final TrueLicenseProperties trueLicenseProperties;

	public EnhancedLicenseManager(TrueLicenseProperties trueLicenseProperties) {
		this.trueLicenseProperties = trueLicenseProperties;
	}

	private static boolean checkAddresses(List<String> addresses, List<String> validAddresses) {
		if (validAddresses == null || validAddresses.isEmpty()) {
			return true;
		}
		if (addresses != null && !addresses.isEmpty()) {
			for (String address : addresses) {
				if (validAddresses.contains(address.trim())) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean checkSerial(String serial, List<String> validSerials) {
		if (validSerials == null || validSerials.isEmpty()) {
			return true;
		}
		if (StringUtils.isNotBlank(serial)) {
			if (validSerials != null && !validSerials.isEmpty()) {
				for (String validSerial : validSerials) {
					if (validSerial.equals(serial)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public byte[] create(LicenseContent content, LicenseNotary notary) throws Exception {
		this.setLicenseParam(false);

		this.initialize(content);
		this.validateBeforeCreate(content);

		GenericCertificate certificate = notary.sign(content);
		return getPrivacyGuard().cert2key(certificate);
	}

	@Override
	public LicenseContent install(final byte[] key, final LicenseNotary notary) throws Exception {
		this.setLicenseParam(true);

		GenericCertificate certificate = getPrivacyGuard().key2cert(key);
		notary.verify(certificate);

		LicenseContent content = (LicenseContent) this.load(certificate.getEncoded());
		this.validate(content);

		this.setLicenseKey(key);
		this.setCertificate(certificate);
		return content;
	}

	@Override
	public synchronized void uninstall() throws Exception {
		this.setLicenseParam(true);

		super.uninstall();
	}

	@Override
	public LicenseContent verify(final LicenseNotary notary) throws Exception {
		byte[] key = getLicenseKey();
		if (null == key) {
			throw new NoLicenseInstalledException(getLicenseParam().getSubject());
		}

		GenericCertificate certificate = getPrivacyGuard().key2cert(key);
		notary.verify(certificate);

		LicenseContent content = (LicenseContent) this.load(certificate.getEncoded());
		this.validate(content);

		this.setCertificate(certificate);
		return content;
	}

	@Override
	public void validate(final LicenseContent content) throws LicenseContentException {
		super.validate(content);

		LicenseContentExtra extra = (LicenseContentExtra) content.getExtra();
		if (extra != null) {
			validateLicenseContentExtra(extra);
		}
	}

	public void setLicenseParam(boolean isPublicKey) {
		KeyStoreParam keyStoreParam = null;
		if (isPublicKey) {
			keyStoreParam =
				new CustomKeyStoreParam(
					EnhancedLicenseManager.class,
					trueLicenseProperties.getPublicKeysStorePath(),
					trueLicenseProperties.getPublicAlias(),
					trueLicenseProperties.getStorePass(),
					null);
		} else {
			keyStoreParam =
				new CustomKeyStoreParam(
					EnhancedLicenseManager.class,
					trueLicenseProperties.getPrivateKeysStorePath(),
					trueLicenseProperties.getPrivateAlias(),
					trueLicenseProperties.getStorePass(),
					trueLicenseProperties.getKeyPass());
		}
		Preferences preferences = Preferences.userNodeForPackage(EnhancedLicenseManager.class);
		CipherParam cipherParam = new DefaultCipherParam(trueLicenseProperties.getStorePass());
		LicenseParam licenseParam =
			new DefaultLicenseParam(
				trueLicenseProperties.getSubject(), preferences, keyStoreParam, cipherParam);
		this.setLicenseParam(licenseParam);
	}

	private void validateBeforeCreate(final LicenseContent content) throws LicenseContentException {
		final Date now = new Date();

		final Date notAfter = content.getNotAfter();
		if (notAfter != null && now.after(notAfter)) {
			throw new LicenseContentException("证书失效时间不能早于当前时间");
		}

		final Date notBefore = content.getNotBefore();
		if (notBefore != null && null != notAfter && notAfter.before(notBefore)) {
			throw new LicenseContentException("证书生效时间不能晚于证书失效时间");
		}

		final String consumerType = content.getConsumerType();
		if (StringUtils.isBlank(consumerType)) {
			throw new LicenseContentException("用户类型不能为空");
		}
	}

	private void validateLicenseContentExtra(LicenseContentExtra extra)
		throws LicenseContentException {
		LicenseOS licenseOS = extra.getLicenseOS();
		if (licenseOS != null) {
			try {
				if (ObjectUtils.isNotNull(licenseOS.getCpuSerial())) {
					String cpuSerial = OSUtils.getCpuSerial();
					if (!checkSerial(cpuSerial, licenseOS.getCpuSerial())) {
						log.warn("当前 CPU 序列号：{}，有效 CPU 序列号：{}", cpuSerial, licenseOS.getCpuSerial());
						throw new LicenseContentException("当前操作系统的 CPU 序列号不在授权范围内");
					}
				}
				if (ObjectUtils.isNotNull(licenseOS.getMainBoardSerial())) {
					String mainBoardSerial = OSUtils.getMainBoardSerial();
					if (!checkSerial(mainBoardSerial, licenseOS.getMainBoardSerial())) {
						log.warn("当前主板序列号：{}，有效主板序列号：{}", mainBoardSerial, licenseOS.getMainBoardSerial());
						throw new LicenseContentException("当前操作系统的主板序列号不在授权范围内");
					}
				}
				if (ObjectUtils.isNotNull(licenseOS.getIpAddress())) {
					List<String> ipAddresses = OSUtils.getIpAddresses();
					if (!checkAddresses(ipAddresses, licenseOS.getIpAddress())) {
						log.warn("当前 IP 地址：{}，有效 IP 地址：{}", ipAddresses, licenseOS.getIpAddress());
						throw new LicenseContentException("当前操作系统的 IP 地址不在授权范围内");
					}
				}
				if (ObjectUtils.isNotNull(licenseOS.getMacAddress())) {
					List<String> macAddresses = OSUtils.getMacAddresses();
					if (!checkAddresses(macAddresses, licenseOS.getMacAddress())) {
						log.warn("当前 MAC 地址：{}，有效 MAC 地址：{}", macAddresses, licenseOS.getMacAddress());
						throw new LicenseContentException("当前操作系统的 MAC 地址不在授权范围内");
					}
				}
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	private Object load(String encoded) {
		BufferedInputStream inputStream = null;
		XMLDecoder decoder = null;
		try {
			inputStream =
				new BufferedInputStream(
					new ByteArrayInputStream(encoded.getBytes(CharsetConstants.UTF_8)));
			decoder = new XMLDecoder(new BufferedInputStream(inputStream, DEFAULT_BUFSIZE), null, null);
			return decoder.readObject();
		} finally {
			try {
				if (decoder != null) {
					decoder.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}
}
