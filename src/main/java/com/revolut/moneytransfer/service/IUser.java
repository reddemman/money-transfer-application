package com.revolut.moneytransfer.service;

import java.util.List;

import com.revolut.moneytransfer.model.User;

public interface IUser {
	
	public List<User> getAllUsers() ;

	public User getUserById(long userId) ;

	public User getUserByName(String userName) ;

	public long insertUser(User user) ;

	public boolean updateUser(Long userId, User user) ;

	public boolean deleteUser(long userId) ;
	

}
