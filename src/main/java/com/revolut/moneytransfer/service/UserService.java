package com.revolut.moneytransfer.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.revolut.moneytransfer.model.User;

@Path("/user")
@Produces("application/json")
public class UserService {

	private IUser userFactory = ResourceFactory.getUserDAO();

	/**
	 * Find by userName
	 * 
	 * @param userName
	 * @return
	 */
	@GET
	@Path("/{userName}")
	public User getUserByName(@PathParam("userName") String userName) {
		final User user = userFactory.getUserByName(userName);
		if (user == null) {
			throw new WebApplicationException("User Not Found", Response.Status.NOT_FOUND);
		}
		return user;
	}

	/**
	 * Find by all
	 * 
	 * @param userName
	 * @return
	 */
	@GET
	@Path("/all")
	public List<User> getAllUsers() {
		return userFactory.getAllUsers();
	}

	/**
	 * Create User
	 * 
	 * @param user
	 * @return
	 */
	@POST
	@Path("/create")
	@Consumes("application/json")
	public User createUser(User user) {
		if (userFactory.getUserByName(user.getUserName()) != null) {
			throw new WebApplicationException("User name already exist", Response.Status.BAD_REQUEST);
		}
		final long uId = userFactory.insertUser(user);
		return userFactory.getUserById(uId);
	}

	/**
	 * Find by User Id
	 * 
	 * @param userId
	 * @param user
	 * @return
	 */
	@PUT
	@Path("/{userId}")
	public Response updateUser(@PathParam("userId") long userId, User user) {
		if (userFactory.updateUser(userId, user)) {
			return Response.status(Response.Status.OK).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

	}

	/**
	 * Delete by User Id
	 * 
	 * @param userId
	 * @return
	 */
	@DELETE
	@Path("/{userId}")
	public Response deleteUser(@PathParam("userId") long userId) {

		if (userFactory.deleteUser(userId)) {
			return Response.status(Response.Status.OK).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

}
