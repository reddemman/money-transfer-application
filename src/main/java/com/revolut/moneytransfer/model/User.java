package com.revolut.moneytransfer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
	 @JsonProperty(required = true)
	private long userId;
	 @JsonProperty(required = true)
	private String userName;
	 @JsonProperty(required = true)
	private String emailAddress;
	 
	public User() {
		
	}

	public User(long userId, String userName, String emailAddress) {
		this.userId = userId;
		this.userName = userName;
		this.emailAddress = emailAddress;
	}
	
	public long getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

}
