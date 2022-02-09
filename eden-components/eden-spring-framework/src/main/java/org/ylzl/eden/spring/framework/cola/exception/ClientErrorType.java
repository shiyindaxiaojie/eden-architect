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

package org.ylzl.eden.spring.framework.cola.exception;

import org.springframework.http.HttpStatus;

/**
 * 客户端断言
 *
 * <ul> 参考《阿里巴巴Java开发手册》错误码
 * <li>A____：表示错误来自于用户</li>
 * <li>B____：表示错误来自于当前系统</li>
 * <li>C____：表示错误来自于第三方服务</li>
 * </ul>
 *
 * @see ServerErrorType
 * @see ThirdServiceErrorType
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public enum ClientErrorType implements ErrorAssert {

	A0001("用户端错误", HttpStatus.BAD_REQUEST.value()),
	A0100("用户注册错误", HttpStatus.BAD_REQUEST.value()),
	A0101("用户未同意隐私协议", HttpStatus.BAD_REQUEST.value()),
	A0102("注册国家或地区受限", HttpStatus.BAD_REQUEST.value()),
	A0110("用户名校验失败", HttpStatus.BAD_REQUEST.value()),
	A0111("用户名已存在", HttpStatus.BAD_REQUEST.value()),
	A0112("用户名包含敏感词", HttpStatus.BAD_REQUEST.value()),
	A0113("用户名包含特殊字符", HttpStatus.BAD_REQUEST.value()),
	A0120("密码校验失败", HttpStatus.BAD_REQUEST.value()),
	A0121("密码长度不够", HttpStatus.BAD_REQUEST.value()),
	A0122("密码强度不够", HttpStatus.BAD_REQUEST.value()),
	A0130("校验码输入错误", HttpStatus.BAD_REQUEST.value()),
	A0131("短信校验码输入错误", HttpStatus.BAD_REQUEST.value()),
	A0132("邮件校验码输入错误", HttpStatus.BAD_REQUEST.value()),
	A0133("语音校验码输入错误", HttpStatus.BAD_REQUEST.value()),
	A0140("用户证件异常", HttpStatus.BAD_REQUEST.value()),
	A0141("用户证件类型未选择", HttpStatus.BAD_REQUEST.value()),
	A0142("大陆身份证编号校验非法", HttpStatus.BAD_REQUEST.value()),
	A0143("护照编号校验非法", HttpStatus.BAD_REQUEST.value()),
	A0144("军官证编号校验非法", HttpStatus.BAD_REQUEST.value()),
	A0150("用户基本信息校验失败", HttpStatus.BAD_REQUEST.value()),
	A0151("手机格式校验失败", HttpStatus.BAD_REQUEST.value()),
	A0152("地址格式校验失败", HttpStatus.BAD_REQUEST.value()),
	A0153("邮箱格式校验失败", HttpStatus.BAD_REQUEST.value()),

	A0200("用户登录异常", HttpStatus.BAD_REQUEST.value()),
	A0201("用户账户不存在", HttpStatus.BAD_REQUEST.value()),
	A0202("用户账户被冻结", HttpStatus.BAD_REQUEST.value()),
	A0203("用户账户已作废", HttpStatus.BAD_REQUEST.value()),
	A0210("用户密码错误", HttpStatus.BAD_REQUEST.value()),
	A0211("用户输入密码错误次数超限", HttpStatus.BAD_REQUEST.value()),
	A0220("用户身份校验失败", HttpStatus.BAD_REQUEST.value()),
	A0221("用户指纹识别失败", HttpStatus.BAD_REQUEST.value()),
	A0222("用户面容识别失败", HttpStatus.BAD_REQUEST.value()),
	A0223("用户未获得第三方登录授权", HttpStatus.BAD_REQUEST.value()),
	A0230("用户登录已过期", HttpStatus.BAD_REQUEST.value()),
	A0240("用户验证码错误", HttpStatus.BAD_REQUEST.value()),
	A0241("用户验证码尝试次数超限", HttpStatus.BAD_REQUEST.value()),

	A0300("访问权限异常", HttpStatus.FORBIDDEN.value()),
	A0301("访问未授权", HttpStatus.FORBIDDEN.value()),
	A0302("正在授权中", HttpStatus.FORBIDDEN.value()),
	A0303("用户授权申请被拒绝", HttpStatus.FORBIDDEN.value()),
	A0310("因访问对象隐私设置被拦截", HttpStatus.FORBIDDEN.value()),
	A0311("授权已过期", HttpStatus.FORBIDDEN.value()),
	A0312("无权限使用 API", HttpStatus.FORBIDDEN.value()),
	A0320("用户访问被拦截", HttpStatus.FORBIDDEN.value()),
	A0321("黑名单用户", HttpStatus.FORBIDDEN.value()),
	A0322("账号被冻结", HttpStatus.FORBIDDEN.value()),
	A0323("非法 IP 地址", HttpStatus.FORBIDDEN.value()),
	A0324("网关访问受限", HttpStatus.FORBIDDEN.value()),
	A0325("地域黑名单", HttpStatus.FORBIDDEN.value()),
	A0330("服务已欠费", HttpStatus.FORBIDDEN.value()),
	A0340("用户签名异常", HttpStatus.FORBIDDEN.value()),
	A0341("RSA 签名错误", HttpStatus.FORBIDDEN.value()),

	A0400("用户请求参数错误", HttpStatus.BAD_REQUEST.value()),
	A0401("包含非法恶意跳转链接", HttpStatus.BAD_REQUEST.value()),
	A0402("无效的用户输入", HttpStatus.BAD_REQUEST.value()),
	A0410("请求必填参数为空", HttpStatus.BAD_REQUEST.value()),
	A0411("用户订单号为空", HttpStatus.BAD_REQUEST.value()),
	A0412("订购数量为空", HttpStatus.BAD_REQUEST.value()),
	A0413("缺少时间戳参数", HttpStatus.BAD_REQUEST.value()),
	A0414("非法的时间戳参数", HttpStatus.BAD_REQUEST.value()),
	A0420("请求参数值超出允许的范围", HttpStatus.BAD_REQUEST.value()),
	A0421("参数格式不匹配", HttpStatus.BAD_REQUEST.value()),
	A0422("地址不在服务范围", HttpStatus.BAD_REQUEST.value()),
	A0423("时间不在服务范围", HttpStatus.BAD_REQUEST.value()),
	A0424("金额超出限制", HttpStatus.BAD_REQUEST.value()),
	A0425("数量超出限制", HttpStatus.BAD_REQUEST.value()),
	A0426("请求批量处理总个数超出限制", HttpStatus.BAD_REQUEST.value()),
	A0427("请求 JSON 解析失败", HttpStatus.BAD_REQUEST.value()),
	A0430("用户输入内容非法", HttpStatus.BAD_REQUEST.value()),
	A0431("包含违禁敏感词", HttpStatus.BAD_REQUEST.value()),
	A0432("图片包含违禁信息", HttpStatus.BAD_REQUEST.value()),
	A0433("文件侵犯版权", HttpStatus.BAD_REQUEST.value()),
	A0440("用户操作异常", HttpStatus.BAD_REQUEST.value()),
	A0441("用户支付超时", HttpStatus.BAD_REQUEST.value()),
	A0442("确认订单超时", HttpStatus.BAD_REQUEST.value()),
	A0443("订单已关闭", HttpStatus.BAD_REQUEST.value()),

	A0500("用户请求服务异常", HttpStatus.BAD_REQUEST.value()),
	A0501("请求次数超出限制", HttpStatus.TOO_MANY_REQUESTS.value()),
	A0502("请求并发数超出限制", HttpStatus.TOO_MANY_REQUESTS.value()),
	A0503("用户操作请等待", HttpStatus.TOO_MANY_REQUESTS.value()),
	A0504("WebSocket 连接异常", HttpStatus.BAD_REQUEST.value()),
	A0505("WebSocket 连接断开", HttpStatus.BAD_REQUEST.value()),
	A0506("用户重复请求", HttpStatus.TOO_MANY_REQUESTS.value()),

	A0600("用户资源异常", HttpStatus.BAD_REQUEST.value()),
	A0601("账户余额不足", HttpStatus.BAD_REQUEST.value()),
	A0602("用户磁盘空间不足", HttpStatus.BAD_REQUEST.value()),
	A0603("用户内存空间不足", HttpStatus.BAD_REQUEST.value()),
	A0604("用户对象存储容量不足", HttpStatus.BAD_REQUEST.value()),
	A0605("用户配额已用光", HttpStatus.BAD_REQUEST.value()),

	A0700("用户上传文件异常", HttpStatus.BAD_REQUEST.value()),
	A0701("用户上传文件类型不匹配", HttpStatus.BAD_REQUEST.value()),
	A0702("用户上传文件太大", HttpStatus.BAD_REQUEST.value()),
	A0703("用户上传图片太大", HttpStatus.BAD_REQUEST.value()),
	A0704("用户上传视频太大", HttpStatus.BAD_REQUEST.value()),
	A0705("用户上传压缩文件太大", HttpStatus.BAD_REQUEST.value()),

	A0800("用户当前版本异常", HttpStatus.BAD_REQUEST.value()),
	A0801("用户安装版本与系统不匹配", HttpStatus.BAD_REQUEST.value()),
	A0802("用户安装版本过低", HttpStatus.BAD_REQUEST.value()),
	A0803("用户安装版本过高", HttpStatus.BAD_REQUEST.value()),
	A0804("用户安装版本已过期", HttpStatus.BAD_REQUEST.value()),
	A0805("用户 API 请求版本不匹配", HttpStatus.BAD_REQUEST.value()),
	A0806("用户 API 请求版本过高", HttpStatus.BAD_REQUEST.value()),
	A0807("用户 API 请求版本过低", HttpStatus.BAD_REQUEST.value()),

	A0900("用户隐私未授权", HttpStatus.BAD_REQUEST.value()),
	A0901("用户隐私未签署", HttpStatus.BAD_REQUEST.value()),
	A0902("用户摄像头未授权", HttpStatus.BAD_REQUEST.value()),
	A0903("用户相机未授权", HttpStatus.BAD_REQUEST.value()),
	A0904("用户图片库未授权", HttpStatus.BAD_REQUEST.value()),
	A0905("用户文件未授权", HttpStatus.BAD_REQUEST.value()),
	A0906("用户位置信息未授权", HttpStatus.BAD_REQUEST.value()),
	A0907("用户通讯录未授权", HttpStatus.BAD_REQUEST.value()),

	A1000("用户设备异常", HttpStatus.BAD_REQUEST.value()),
	A1001("用户相机异常", HttpStatus.BAD_REQUEST.value()),
	A1002("用户麦克风异常", HttpStatus.BAD_REQUEST.value()),
	A1003("用户听筒异常", HttpStatus.BAD_REQUEST.value()),
	A1004("用户扬声器异常", HttpStatus.BAD_REQUEST.value()),
	A1005("用户 GPS 定位异常", HttpStatus.BAD_REQUEST.value());

	private final String errCode;

	private final String errMessage;

	private final int httpStatusCode;

	ClientErrorType(String errMessage, int httpStatusCode) {
		this.errCode = this.name();
		this.errMessage = errMessage;
		this.httpStatusCode = httpStatusCode;
	}

	@Override
	public String getErrCode() {
		return errCode;
	}

	@Override
	public String getErrMessage() {
		return errMessage;
	}

	@Override
	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	@Override
	public String toString() {
		return errMessage;
	}

	@Override
	public void throwNewException(Throwable e) {
		throw new ClientException(this);
	}
}
