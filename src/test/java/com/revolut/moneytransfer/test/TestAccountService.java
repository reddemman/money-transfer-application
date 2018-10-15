package com.revolut.moneytransfer.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.revolut.moneytransfer.model.Account;

public class TestAccountService extends TestService {

	// Scenario: test get all user accounts return 200 OK
	@Test
	public void testGetAllAccounts() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/account/all").build();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
		// check the content
		String jsonString = EntityUtils.toString(response.getEntity());
		Account[] accounts = mapper.readValue(jsonString, Account[].class);
		assertTrue(accounts.length > 0);
	}

	// Scenario: test get account balance given account ID return 200 OK
	@Test
	public void testGetAccountBalance() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/account/1/balance").build();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
		// check the content, assert user test2 have balance 100
		String balance = EntityUtils.toString(response.getEntity());
		BigDecimal res = new BigDecimal(balance).setScale(4, RoundingMode.HALF_EVEN);
		BigDecimal db = new BigDecimal(10).setScale(4, RoundingMode.HALF_EVEN);
		assertTrue(res.equals(db));
	}

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

	// Scenario: delete valid user account return 200 OK
	@Test
	public void testDeleteAccount() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/account/2").build();
		HttpDelete request = new HttpDelete(uri);
		request.setHeader("Content-type", "application/json");
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
	}

	// Scenario: test delete non-existent account. return 404 NOT FOUND 	 
	@Test
	public void testDeleteNonExistingAccount() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/account/300").build();
		HttpDelete request = new HttpDelete(uri);
		request.setHeader("Content-type", "application/json");
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 404);
	}

}
