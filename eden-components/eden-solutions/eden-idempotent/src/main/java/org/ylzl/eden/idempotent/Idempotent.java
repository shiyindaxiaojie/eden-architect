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

package org.ylzl.eden.idempotent;

import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.idempotent.strategy.IdempotentStrategy;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 幂等性标注
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Documented
@Inherited
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Idempotent {

	IdempotentStrategy strategy() default IdempotentStrategy.TTL;

	String key() default Strings.EMPTY;

	long ttl() default 10L;

	TimeUnit timeUnit() default TimeUnit.SECONDS;
}
