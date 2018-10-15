package com.revolut.moneytransfer.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.revolut.moneytransfer.model.Account;
import com.revolut.moneytransfer.model.Transaction;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestTransactionService extends TestService {
	
	
	// Scenario: test create new user account return 200 OK
	@Test
	public void testCreateAccount() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/account/create").build();
		BigDecimal balance = new BigDecimal(10).setScale(4, RoundingMode.HALF_EVEN);
		Account acc1 = new Account(1, "test2", balance, "CNY");
		String jsonInString = mapper.writeValueAsString(acc1);
		StringEntity entity = new StringEntity(jsonInString);
		HttpPut request = new HttpPut(uri);
		request.setHeader("Content-type", "application/json");
		request.setEntity(entity);		
		
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
		Account acc2 = new Account(2, "test2", balance, "CNY");
		jsonInString = mapper.writeValueAsString(acc2);
		entity = new StringEntity(jsonInString);
		request.setEntity(entity);
		client.execute(request);
		String jsonString = EntityUtils.toString(response.getEntity());		
		Account aAfterCreation = mapper.readValue(jsonString, Account.class);
		assertTrue(aAfterCreation.getUserName().equals("test2"));
		assertTrue(aAfterCreation.getCurrencyCode().equals("CNY"));
	}
	
	// Scenario: test deposit money to given account number return 200 OK
	@Test
	public void testDeposit() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/account/1/deposit/100").build();
		HttpPut request = new HttpPut(uri);
		request.setHeader("Content-type", "application/json");
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
		String jsonString = EntityUtils.toString(response.getEntity());
		Account afterDeposit = mapper.readValue(jsonString, Account.class);
		// check balance is increased from 100 to 200
		assertTrue(afterDeposit.getBalance().equals(new BigDecimal(110).setScale(4, RoundingMode.HALF_EVEN)));

	}

	//Scenario: test withdraw money from account given account number, account has sufficient fund return 200 OK	
	@Test
	public void testWithDrawSufficientFund() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/account/2/withdraw/5").build();
		HttpPut request = new HttpPut(uri);
		request.setHeader("Content-type", "application/json");
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
		String jsonString = EntityUtils.toString(response.getEntity());
		Account afterDeposit = mapper.readValue(jsonString, Account.class);
		// check balance is decreased from 200 to 100
		assertTrue(afterDeposit.getBalance().equals(new BigDecimal(15).setScale(4, RoundingMode.HALF_EVEN)));

	}

	// Scenario: test withdraw money from account given account number, 400 error 
	@Test
	public void testWithDrawNonSufficientFund() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/account/2/withdraw/1000.23456").build();
		HttpPut request = new HttpPut(uri);
		request.setHeader("Content-type", "application/json");
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();

		assertTrue(statusCode == 400);

	}
	// Scenario: test transaction from one account to another with source account
	// has sufficient fund return 200 OK
	@Test
	public void testTransactionEnoughFund() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/transaction").build();
		BigDecimal amount = new BigDecimal(10).setScale(4, RoundingMode.HALF_EVEN);
		Transaction transaction = new Transaction("CNY", amount, 1L, 2L);

		String jsonInString = mapper.writeValueAsString(transaction);
		StringEntity entity = new StringEntity(jsonInString);
		HttpPost request = new HttpPost(uri);
		request.setHeader("Content-type", "application/json");
		request.setEntity(entity);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
	}

	// Scenario: test transaction from one account to another with source account
	// has no sufficient fund return 400
	@Test
	public void testTransactionNotEnoughFund() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/transaction").build();
		BigDecimal amount = new BigDecimal(100000).setScale(4, RoundingMode.HALF_EVEN);
		Transaction transaction = new Transaction("EUR", amount, 1L, 2L);

		String jsonInString = mapper.writeValueAsString(transaction);
		StringEntity entity = new StringEntity(jsonInString);
		HttpPost request = new HttpPost(uri);
		request.setHeader("Content-type", "application/json");
		request.setEntity(entity);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 400);
	}

	// Scenario: test transaction from one account to another with
	// source/destination account with different currency code return 400
	@Test
	public void testTransactionDifferentCcy() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/transaction").build();
		BigDecimal amount = new BigDecimal(100).setScale(4, RoundingMode.HALF_EVEN);
		Transaction transaction = new Transaction("USD", amount, 3L, 4L);

		String jsonInString = mapper.writeValueAsString(transaction);
		StringEntity entity = new StringEntity(jsonInString);
		HttpPost request = new HttpPost(uri);
		request.setHeader("Content-type", "application/json");
		request.setEntity(entity);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 400);

	}

}
