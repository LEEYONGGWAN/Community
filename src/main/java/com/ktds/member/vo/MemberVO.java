package com.ktds.member.vo;

import javax.validation.constraints.NotEmpty;

public class MemberVO {

	private int id;
	
	@NotEmpty(message="Email은 필수 입력값 입니다.")
	private String email;
	
	@NotEmpty(message="Password은 필수 입력값 입니다.")
	private String password;
	
	@NotEmpty(message="Nickname은 필수 입력값 입니다.")
	private String nickname;

	private String registDate;
	
	//암호화
	private String salt;




	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getRegistDate() {
		return registDate;
	}

	public void setRegistDate(String registDate) {
		this.registDate = registDate;
	}
	
	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
	
}
