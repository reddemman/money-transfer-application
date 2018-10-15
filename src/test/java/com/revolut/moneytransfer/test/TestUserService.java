package com.revolut.moneytransfer.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.revolut.moneytransfer.model.User;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestUserService extends TestService {

	// Scenario: Create user using JSON return
	@Test
	public void testCreateUser() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/user/create").build();
		User user = new User(1, "test1", "test1@gmail.com");
		String jsonInString = mapper.writeValueAsString(user);
		StringEntity entity = new StringEntity(jsonInString);
		HttpPost request = new HttpPost(uri);
		request.setHeader("Content-type", "application/json");
		request.setEntity(entity);
		HttpResponse response = client.execute(request);
		User user2 = new User(2, "test2", "test2@gmail.com");
		jsonInString = mapper.writeValueAsString(user2);
		entity = new StringEntity(jsonInString);
		request.setHeader("Content-type", "application/json");
		request.setEntity(entity);
		client.execute(request);
		User user3 = new User(3, "test3", "test3@gmail.com");
		jsonInString = mapper.writeValueAsString(user3);
		entity = new StringEntity(jsonInString);
		request.setHeader("Content-type", "application/json");
		request.setEntity(entity);
		client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
		String jsonString = EntityUtils.toString(response.getEntity());
		User uAfterCreation = mapper.readValue(jsonString, User.class);
		assertTrue(uAfterCreation.getUserName().equals("test1"));
		assertTrue(uAfterCreation.getEmailAddress().equals("test1@gmail.com"));
	}

	// Scenario: test get user by given user name return 200 OK
	@Test
	public void testGetUser() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/user/test2").build();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();

		assertTrue(statusCode == 200);
		// check the content
		String jsonString = EntityUtils.toString(response.getEntity());
		User user = mapper.readValue(jsonString, User.class);
		assertTrue(user.getUserName().equals("test2"));
		assertTrue(user.getEmailAddress().equals("test2@gmail.com"));

	}

	// Scenario: test get all users return 200
	@Test
	public void testGetAllUsers() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/user/all").build();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
		// check the content
		String jsonString = EntityUtils.toString(response.getEntity());
		User[] users = mapper.readValue(jsonString, User[].class);
		assertTrue(users.length > 0);
	}

	// Scenario: Create user already existed using JSON return 400
	@Test
	public void testCreateUserExisting() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/user/create").build();
		User user = new User(2, "test1", "test1@gmail.com");
		String jsonInString = mapper.writeValueAsString(user);
		StringEntity entity = new StringEntity(jsonInString);
		HttpPost request = new HttpPost(uri);
		request.setHeader("Content-type", "application/json");
		request.setEntity(entity);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 400);

	}

	// Scenario: Update Existing User using JSON provided from client return 200 OK
	@Test
	public void testUpdateUser() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/user/2").build();
		User user = new User(2L, "test1", "test1123@gmail.com");
		String jsonInString = mapper.writeValueAsString(user);
		StringEntity entity = new StringEntity(jsonInString);
		HttpPut request = new HttpPut(uri);
		request.setHeader("Content-type", "application/json");
		request.setEntity(entity);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
	}

	// Scenario: Update non existed User using JSON provided from client return 404
	@Test
	public void testUpdateNonExistingUser() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/user/100").build();
		User user = new User(2L, "test1", "test1123@gmail.com");
		String jsonInString = mapper.writeValueAsString(user);
		StringEntity entity = new StringEntity(jsonInString);
		HttpPut request = new HttpPut(uri);
		request.setHeader("Content-type", "application/json");
		request.setEntity(entity);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 404);
	}

	// Scenario: test delete user return 200
	@Test
	public void testDeleteUser() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/user/3").build();
		HttpDelete request = new HttpDelete(uri);
		request.setHeader("Content-type", "application/json");
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
	}

	// Scenario: test delete non-existed user return 404 NOT FOUND
	@Test
	public void testDeleteNonExistingUser() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/user/300").build();
		HttpDelete request = new HttpDelete(uri);
		request.setHeader("Content-type", "application/json");
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 404);
	}

}
