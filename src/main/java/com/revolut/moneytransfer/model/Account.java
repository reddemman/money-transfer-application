package com.revolut.moneytransfer.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Account {
	
	 @JsonProperty(required = true)
	private long accountId;
	 @JsonProperty(required = true)
	private String userName;
	 @JsonProperty(required = true)
	private BigDecimal balance;
	 @JsonProperty(required = true)
	private String currencyCode;	
	
	public Account() {
		
	}

	public Account(long accountId, String userName, BigDecimal balance, String currencyCode) {
		this.accountId = accountId;
		this.userName = userName;
		this.balance = balance;
		this.currencyCode = currencyCode;
	}
	
	public long getAccountId() {
		return accountId;
	}

	public String getUserName() {
		return userName;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	

}
