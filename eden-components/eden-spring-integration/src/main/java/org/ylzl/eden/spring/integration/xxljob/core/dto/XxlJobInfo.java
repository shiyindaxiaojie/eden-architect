package org.ylzl.eden.spring.integration.xxljob.core.dto;

import lombok.*;

import java.util.Date;

/**
 * XxlJob 任务
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@Data
public class XxlJobInfo {

	private int id;

	private int jobGroup;

	private String jobCron;

	private String jobDesc;

	private Date addTime;

	private Date updateTime;

	private String author;

	private String alarmEmail;

	private String executorRouteStrategy;

	private String executorHandler;

	private String executorParam;

	private String executorBlockStrategy;

	private int executorTimeout;

	private int executorFailRetryCount;

	private String glueType;

	private String glueSource;

	private String glueRemark;

	private Date glueUpdateTime;

	private String childJobId;

	private int triggerStatus;

	private long triggerLastTime;

	private long triggerNextTime;
}
