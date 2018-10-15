package com.revolut.moneytransfer.service;

import java.math.BigDecimal;
import java.util.List;

import com.revolut.moneytransfer.model.Account;
import com.revolut.moneytransfer.model.Transaction;

public interface IAccount {
	public long createAccount(Account account);

	public List<Account> getAllAccounts();

	public Account getAccountById(long accountId);

	public boolean deleteAccountById(long accountId);

	public boolean updateAccountBalance(long accountId, BigDecimal deltaAmount);

	public boolean transferAccountBalance(Transaction userTransaction) throws Exception;

}
