package com.revolut.moneytransfer.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.revolut.moneytransfer.model.Transaction;

@Path("/transaction")

public class TransactionService {
	
	IAccount account = ResourceFactory.getAccountDAO();

	/**
	 * Transfer fund between two accounts.
	 * 
	 * @param transaction
	 * @return
	 * @throws Exception 	 
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response transferFund(Transaction transaction) throws Exception {

		if (account.transferAccountBalance(transaction)) {
			return Response.status(Response.Status.OK).build();
		} else {
			// transaction failed
			throw new WebApplicationException("Transaction failed", Response.Status.BAD_REQUEST);
		}

	}

}
