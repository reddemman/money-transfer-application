package com.revolut.moneytransfer.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Transaction {

	@JsonProperty(required = true)
	private String currencyCode;
	@JsonProperty(required = true)
	private BigDecimal amount;
	@JsonProperty(required = true)
	private Long fromAccountId;
	@JsonProperty(required = true)
	private Long toAccountId;

	public Transaction() {

	}

	public Transaction(String currencyCode, BigDecimal amount, Long fromAccountId, Long toAccountId) {
		this.currencyCode = currencyCode;
		this.amount = amount;
		this.fromAccountId = fromAccountId;
		this.toAccountId = toAccountId;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public Long getFromAccountId() {
		return fromAccountId;
	}

	public Long getToAccountId() {
		return toAccountId;
	}
}
