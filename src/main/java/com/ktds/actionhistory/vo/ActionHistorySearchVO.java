package com.ktds.actionhistory.vo;

public class ActionHistorySearchVO {

	private int pageNo;
	private String startDate;
	private String endDate;
	private String requestType;
	private String ip;
	private String email;
	private String nickname;
	private String log;
	private String asIs;
	private String toBe;

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public String getAsIs() {
		return asIs;
	}

	public void setAsIs(String asIs) {
		this.asIs = asIs;
	}

	public String getToBe() {
		return toBe;
	}

	public void setToBe(String toBe) {
		this.toBe = toBe;
	}

}
