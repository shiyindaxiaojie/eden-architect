package org.ylzl.eden.xxljob.spring.boot.model;

import lombok.*;

import java.util.Date;

/**
 * XxlJob 任务
 *
 * @author <a href="mailto:guoyuanlu@puyiwm.com">gyl</a>
 * @since 2.4.13
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
