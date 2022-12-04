package org.ylzl.eden.dynamic.sms.single;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.ylzl.eden.dynamic.sms.model.SmsModel;

import java.io.Serializable;

/**
 * 发送单条短信请求
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@SuperBuilder
//@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@Data
public class SingleSendSmsRequest extends SmsModel implements Serializable {

}
