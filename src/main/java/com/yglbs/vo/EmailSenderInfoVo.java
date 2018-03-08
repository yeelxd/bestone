package com.yglbs.vo;

import java.util.Map;

//邮件发送信息包装类
public class EmailSenderInfoVo {

	// 发送邮件的服务器的Host和端口
	private String serverHost;
	private int serverPort;
	// 邮件发送者的地址
	private String fromAddress;
	// 邮件接收者的地址组
	private String[] toAddressArray;
	// 邮件抄送者邮箱
	private String[] ccAddressArray;
	// 是否需要身份验证
	private boolean validate;
	// 登陆邮件发送服务器的用户名和密码
	private String userName;
	private String password;
	// 邮件主题
	private String subject;
	// 邮件的文本内容
	private String content;
	// 邮件附件的文件名
	private String[] attachFileNames;
	// Velocity Template
	private String vmFileName;
	// 模板中需要替换的数据
	private Map<String,Object> dataModels;

	public String getServerHost() {
		return serverHost;
	}

	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	
	public String[] getToAddressArray() {
		return toAddressArray;
	}

	public void setToAddressArray(String[] toAddressArray) {
		this.toAddressArray = toAddressArray;
	}

	public String[] getCcAddressArray() {
		return ccAddressArray;
	}

	public void setCcAddressArray(String[] ccAddressArray) {
		this.ccAddressArray = ccAddressArray;
	}

	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String[] getAttachFileNames() {
		return attachFileNames;
	}

	public void setAttachFileNames(String[] attachFileNames) {
		this.attachFileNames = attachFileNames;
	}

	public String getVmFileName() {
		return vmFileName;
	}

	public void setVmFileName(String vmFileName) {
		this.vmFileName = vmFileName;
	}

	public Map<String, Object> getDataModels() {
		return dataModels;
	}

	public void setDataModels(Map<String, Object> dataModels) {
		this.dataModels = dataModels;
	}

}
