package com.revolut.moneytransfer.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.revolut.moneytransfer.model.User;

public class UserImpl implements IUser {

	private HashMap<Long, User> userMap = new HashMap<Long, User>();

	public List<User> getAllUsers() {
		List<User> userList = new ArrayList<User>();
		for (User user : userMap.values()) {
			userList.add(user);
		}
		return userList;
	}

	public User getUserById(long userId) {
		return userMap.get(userId);
	}

	public User getUserByName(String userName) {
		for (User user : userMap.values()) {
			if (user.getUserName().equals(userName)) {
				return user;
			}

		}
		return null;
	}

	public long insertUser(User user) {
		userMap.put(user.getUserId(), user);
		return user.getUserId();
	}

	public boolean updateUser(Long userId, User user) {
		User tagetUser = getUserById(userId);
		if (tagetUser != null) {
			userMap.put(userId, user);
			return true;
		}

		return false;
	}

	public boolean deleteUser(long userId) {
		User tagetUser = getUserById(userId);
		if (tagetUser != null) {
			userMap.remove(userId);
			return true;
		}

		return false;

	}

}
