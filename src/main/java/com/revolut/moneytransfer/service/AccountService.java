package com.revolut.moneytransfer.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.revolut.moneytransfer.model.Account;

@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountService {
	
	private BigDecimal ZERO = new BigDecimal(0).setScale(4, RoundingMode.HALF_UP);	
	
	private IAccount accountService = ResourceFactory.getAccountDAO();	

	@POST
	@Path("/create")	
	@Consumes(MediaType.APPLICATION_JSON)
	public Account createAccount(Account account) {
		final long accountId = accountService.createAccount(account);
		return accountService.getAccountById(accountId);
	}

	@GET
	@Path("/all")	 
	public List<Account> getAllAccounts() {
		return accountService.getAllAccounts();
	}

	@GET
	@Path("/{accountId}")	
	public Account getAccount(@PathParam("accountId") long accountId) {
		return accountService.getAccountById(accountId);
	}

	@GET
	@Path("/{accountId}/balance")
	public BigDecimal getBalance(@PathParam("accountId") long accountId) {
		final Account account = accountService.getAccountById(accountId);
		if (account == null) {
			throw new WebApplicationException("Account not found", Response.Status.NOT_FOUND);
		}
		return account.getBalance();
	}

	@PUT
	@Path("/{accountId}/deposit/{amount}")	
	public Account deposit(@PathParam("accountId") long accountId, @PathParam("amount") BigDecimal amount) {
		if (amount.compareTo(ZERO) <= 0) {
			throw new WebApplicationException("Invalid Deposit amount", Response.Status.BAD_REQUEST);
		}
		accountService.updateAccountBalance(accountId, amount.setScale(4, RoundingMode.HALF_EVEN));
		return accountService.getAccountById(accountId);
	}

	/**
	 * Withdraw amount by account Id
	 * 
	 * @param accountId
	 * @param amount
	 * @return
	 */
	@PUT
	@Path("/{accountId}/withdraw/{amount}")
	
	public Account withdraw(@PathParam("accountId") long accountId, @PathParam("amount") BigDecimal amount) {

		if (amount.compareTo(ZERO) <= 0) {
			throw new WebApplicationException("Invalid Deposit amount", Response.Status.BAD_REQUEST);
		}

		BigDecimal delta = amount.negate();
		if (!accountService.updateAccountBalance(accountId, delta.setScale(4, RoundingMode.HALF_EVEN))) {
			throw new WebApplicationException("Not sufficient Fund", Response.Status.BAD_REQUEST);
		}
		return accountService.getAccountById(accountId);
	}

	/**
	 * Delete amount by account Id
	 * 
	 * @param accountId
	 * @param amount
	 * @return
	 */
	@DELETE
	@Path("/{accountId}")
	public Response deleteAccount(@PathParam("accountId") long accountId) {

		if (accountService.deleteAccountById(accountId)) {
			return Response.status(Response.Status.OK).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

}
