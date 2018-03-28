package com.ktds.actionhistory.vo;

import io.github.seccoding.web.pager.annotations.EndRow;
import io.github.seccoding.web.pager.annotations.StartRow;

public class ActionHistorySearchVO {

	private int pageNo = -1;

	@StartRow
	private int startRow;

	@EndRow
	private int endRow;

	private String startDate;
	private String startDateYear;
	private String startDateMonth;
	private String startDateDate;

	private String endDate;
	private String endDateYear;
	private String endDateMonth;
	private String endDateDate;

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

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public int getEndRow() {
		return endRow;
	}

	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStartDateYear() {
		return startDateYear;
	}

	public void setStartDateYear(String startDateYear) {
		this.startDateYear = startDateYear;
	}

	public String getStartDateMonth() {
		return startDateMonth;
	}

	public void setStartDateMonth(String startDateMonth) {
		this.startDateMonth = startDateMonth;
	}

	public String getStartDateDate() {
		return startDateDate;
	}

	public void setStartDateDate(String startDateDate) {
		this.startDateDate = startDateDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEndDateYear() {
		return endDateYear;
	}

	public void setEndDateYear(String endDateYear) {
		this.endDateYear = endDateYear;
	}

	public String getEndDateMonth() {
		return endDateMonth;
	}

	public void setEndDateMonth(String endDateMonth) {
		this.endDateMonth = endDateMonth;
	}

	public String getEndDateDate() {
		return endDateDate;
	}

	public void setEndDateDate(String endDateDate) {
		this.endDateDate = endDateDate;
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
