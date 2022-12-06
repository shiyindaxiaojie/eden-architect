package org.ylzl.eden.dynamic.sms.model.multi;

import com.google.common.collect.Lists;
import lombok.*;
import org.ylzl.eden.dynamic.sms.model.Sms;

import java.io.Serializable;
import java.util.List;

/**
 * 个性化群发短信请求
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Data
public class MultiSendSmsRequest implements Serializable {

	/**
	 * 个性化群发短信
	 */
	private List<Sms> smsList = Lists.newArrayList();

	/**
	 * 添加群发对象
	 *
	 * @param sms
	 */
	public void addSendSms(Sms sms) {
		smsList.add(sms);
	}
}
