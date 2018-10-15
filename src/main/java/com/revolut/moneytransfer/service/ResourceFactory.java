package com.revolut.moneytransfer.service;

public class ResourceFactory {

	private final static UserImpl userDAO = new UserImpl();
	private final static AccountImpl accountDAO = new AccountImpl();

	public static UserImpl getUserDAO() {
		return userDAO;
	}

	public static AccountImpl getAccountDAO() {
		return accountDAO;
	}

}
