package org.ylzl.eden.common.sms.core.multi;

import com.google.common.collect.Lists;
import lombok.*;
import org.ylzl.eden.common.sms.core.SmsModel;

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
	private List<SmsModel> smsModelList = Lists.newArrayList();

	/**
	 * 添加群发对象
	 *
	 * @param smsModel
	 */
	public void addSendSms(SmsModel smsModel) {
		smsModelList.add(smsModel);
	}
}
