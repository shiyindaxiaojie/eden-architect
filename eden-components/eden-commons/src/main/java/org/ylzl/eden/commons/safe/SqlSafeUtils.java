/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.commons.safe;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SQL 安全工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class SqlSafeUtils {

	private static final String SQL_TYPES =
		"TABLE, TABLESPACE, PROCEDURE, FUNCTION, TRIGGER, KEY, VIEW, MATERIALIZED VIEW, LIBRARY"
			+ "DATABASE LINK, DBLINK, INDEX, CONSTRAINT, TRIGGER, USER, SCHEMA, DATABASE, PLUGGABLE DATABASE, BUCKET, "
			+ "CLUSTER, COMMENT, SYNONYM, TYPE, JAVA, SESSION, ROLE, PACKAGE, PACKAGE BODY, OPERATOR"
			+ "SEQUENCE, RESTORE POINT, PFILE, CLASS, CURSOR, OBJECT, RULE, USER, DATASET, DATASTORE, "
			+ "COLUMN, FIELD, OPERATOR";

	private static final String[] SQL_REGEXPS = {
		"(?i)(.*)(\\b)+(OR|AND)(\\s)+(true|false)(\\s)*(.*)",
		"(?i)(.*)(\\b)+(OR|AND)(\\s)+(\\w)(\\s)*(\\=)(\\s)*(\\w)(\\s)*(.*)",
		"(?i)(.*)(\\b)+(OR|AND)(\\s)+(equals|not equals)(\\s)+(true|false)(\\s)*(.*)",
		"(?i)(.*)(\\b)+(OR|AND)(\\s)+([0-9A-Za-z_'][0-9A-Za-z\\d_']*)(\\s)*(\\=)(\\s)*"
			+ "([0-9A-Za-z_'][0-9A-Za-z\\d_']*)(\\s)*(.*)",
		"(?i)(.*)(\\b)+(OR|AND)(\\s)+([0-9A-Za-z_'][0-9A-Za-z\\d_']*)(\\s)*(\\!\\=)(\\s)*"
			+ "([0-9A-Za-z_'][0-9A-Za-z\\d_']*)(\\s)*(.*)",
		"(?i)(.*)(\\b)+(OR|AND)(\\s)+([0-9A-Za-z_'][0-9A-Za-z\\d_']*)(\\s)*(\\<\\>)(\\s)*"
			+ "([0-9A-Za-z_'][0-9A-Za-z\\d_']*)(\\s)*(.*)",
		"(?i)(.*)(\\b)+SELECT(\\b)+\\s.*(\\b)(.*)",
		"(?i)(.*)(\\b)+INSERT(\\b)+\\s.*(\\b)+INTO(\\b)+\\s.*(.*)",
		"(?i)(.*)(\\b)+UPDATE(\\b)+\\s.*(.*)",
		"(?i)(.*)(\\b)+DELETE(\\b)+\\s.*(\\b)+FROM(\\b)+\\s.*(.*)",
		"(?i)(.*)(\\b)+UPSERT(\\b)+\\s.*(.*)",
		"(?i)(.*)(\\b)+SAVEPOINT(\\b)+\\s.*(.*)",
		"(?i)(.*)(\\b)+CALL(\\b)+\\s.*(.*)",
		"(?i)(.*)(\\b)+ROLLBACK(\\b)+\\s.*(.*)",
		"(?i)(.*)(\\b)+KILL(\\b)+\\s.*(.*)",
		"(?i)(.*)(\\b)+DROP(\\b)+\\s.*(.*)",
		"(?i)(.*)(\\b)+CREATE(\\b)+(\\s)*(" + SQL_TYPES.replaceAll(",", "|") + ")(\\b)+\\s.*(.*)",
		"(?i)(.*)(\\b)+ALTER(\\b)+(\\s)*(" + SQL_TYPES.replaceAll(",", "|") + ")(\\b)+\\s.*(.*)",
		"(?i)(.*)(\\b)+TRUNCATE(\\b)+(\\s)*(" + SQL_TYPES.replaceAll(",", "|") + ")(\\b)+\\s.*(.*)",
		"(?i)(.*)(\\b)+LOCK(\\b)+(\\s)*(" + SQL_TYPES.replaceAll(",", "|") + ")(\\b)+\\s.*(.*)",
		"(?i)(.*)(\\b)+UNLOCK(\\b)+(\\s)*(" + SQL_TYPES.replaceAll(",", "|") + ")(\\b)+\\s.*(.*)",
		"(?i)(.*)(\\b)+RELEASE(\\b)+(\\s)*(" + SQL_TYPES.replaceAll(",", "|") + ")(\\b)+\\s.*(.*)",
		"(?i)(.*)(\\b)+DESC(\\b)+(\\w)*\\s.*(.*)",
		"(?i)(.*)(\\b)+DESCRIBE(\\b)+(\\w)*\\s.*(.*)",
		"(.*)(/\\*|\\*/|;){1,}(.*)",
		"(.*)(-){2,}(.*)",
	};

	private static final List<Pattern> validationPatterns = buildPatterns(SQL_REGEXPS);

	public static boolean isSQLInjectionSafe(String dataString) {
		if (StringUtils.isBlank(dataString)) {
			return true;
		}

		for (Pattern pattern : validationPatterns) {
			if (matches(pattern, dataString)) {
				return false;
			}
		}
		return true;
	}

	private static boolean matches(Pattern pattern, String dataString) {
		Matcher matcher = pattern.matcher(dataString);
		return matcher.matches();
	}

	private static List<Pattern> buildPatterns(String[] expressionStrings) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		for (String expression : expressionStrings) {
			patterns.add(getPattern(expression));
		}
		return patterns;
	}

	private static Pattern getPattern(String regEx) {
		return Pattern.compile(regEx, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	}
}
