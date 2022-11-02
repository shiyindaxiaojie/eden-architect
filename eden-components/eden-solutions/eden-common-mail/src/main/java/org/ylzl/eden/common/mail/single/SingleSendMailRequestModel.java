package org.ylzl.eden.common.mail.single;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.ylzl.eden.common.mail.MailModel;

import java.io.Serializable;

/**
 * 单条发送邮件请求
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@SuperBuilder
@NoArgsConstructor
//@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@Data
public class SingleSendMailRequestModel extends MailModel implements Serializable {


}
