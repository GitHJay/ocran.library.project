package com.ocran;

public class Member {
	private String memberID;
	private String memberName;
	private String phoneNumber;
	private String email;
	
	public Member(String memberID, String memberName, String phoneNumber, String email) {
		this.memberID		= memberID;
		this.memberName		= memberName;
		this.phoneNumber	= phoneNumber;
		this.email			= email;
	}
	
	public String getMemberID() {
		return memberID;
	}
	
	public String getMemberName() {
		return memberName;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public String getEmail() {
		return email;
	}
}
