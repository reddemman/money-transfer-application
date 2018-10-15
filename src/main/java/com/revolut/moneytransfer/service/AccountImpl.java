package com.revolut.moneytransfer.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.revolut.moneytransfer.model.Account;
import com.revolut.moneytransfer.model.Transaction;

public class AccountImpl implements IAccount {

	private HashMap<Long, Account> accountMap = new HashMap<Long, Account>();

	public long createAccount(Account account) {

		accountMap.put(account.getAccountId(), account);
		return account.getAccountId();

	}

	public List<Account> getAllAccounts() {
		List<Account> accountList = new ArrayList<Account>();
		for (Account account : accountMap.values()) {
			accountList.add(account);
		}
		return accountList;
	}

	public Account getAccountById(long accountId) {
		return accountMap.get(accountId);
	}

	public boolean deleteAccountById(long accountId) {
		if (getAccountById(accountId) != null) {
			accountMap.remove(accountId);
			return true;
		}

		return false;
	}

	public boolean updateAccountBalance(long accountId, BigDecimal deltaAmount) {
		Account targetAccount = accountMap.get(accountId);
		if (targetAccount != null) {
			BigDecimal balance = targetAccount.getBalance().add(deltaAmount);
			if (balance.compareTo(new BigDecimal(0).setScale(4, RoundingMode.HALF_UP)) < 0) {
				return false;
			}
			accountMap.put(accountId, new Account(targetAccount.getAccountId(), targetAccount.getUserName(), balance,
					targetAccount.getCurrencyCode()));
			return true;
		}

		return false;
	}

	public boolean transferAccountBalance(Transaction userTransaction) throws Exception {
		Account fromAccount = accountMap.get(userTransaction.getFromAccountId());
		Account toAccount = accountMap.get(userTransaction.getToAccountId());

		// check locking status
		if (fromAccount == null || toAccount == null) {
			throw new Exception("Fail to lock both accounts for write");
		}
		// check transaction currency
		if (!fromAccount.getCurrencyCode().equals(userTransaction.getCurrencyCode())) {
			throw new Exception("Fail to transfer Fund, transaction ccy are different from source/destination");

		}

		BigDecimal fromAccountLeftOver = fromAccount.getBalance().subtract(userTransaction.getAmount());
		BigDecimal toAccountLeftOver = toAccount.getBalance().add(userTransaction.getAmount());

		accountMap.put(userTransaction.getFromAccountId(), new Account(fromAccount.getAccountId(),
				fromAccount.getUserName(), fromAccountLeftOver, fromAccount.getCurrencyCode()));
		accountMap.put(userTransaction.getToAccountId(), new Account(toAccount.getAccountId(), toAccount.getUserName(),
				toAccountLeftOver, toAccount.getCurrencyCode()));

		if (fromAccountLeftOver.compareTo(new BigDecimal(0).setScale(4, RoundingMode.HALF_UP)) < 0) {
			throw new Exception("Not enough Fund from source Account ");
		}

		return true;
	}

}
