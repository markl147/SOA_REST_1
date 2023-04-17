package resource;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.EntityTag;


import dao.bank.BankAccount;
import dao.bank.BankDaoDB;

@Path("/accounts")
public class BankResourceDB {
	
	@Context
	private UriInfo context;
	
	
	
	@DELETE
	@Produces(MediaType.TEXT_HTML)
	public Response deleteAllBackAccounts(@Context HttpServletResponse servletResponse) {

		int result = BankDaoDB.instance.deleteAllAccounts();

		if (result == 1) {
			return Response.status(200).entity("NO_CONTENT").build();
		}
		else if(result == -1) {
			return Response.status(500).entity("INTERNAL_SERVER_ERROR").build();
		}
		else {
			return Response.status(404).entity("NOT_FOUND").build();
		}
		
	}
	

	
//	@GET
//	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//	public List<BankAccount> getBankAccounts() {
//		System.out.println("Hello ... Testing");
//		return BankDaoDB.instance.getAllAccounts();
//	}
	
//	@GET
//	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//	public Response getBankAccounts() {
//	    System.out.println("Hello ... Testing");
//	    List<BankAccount> bankAccounts = BankDaoDB.instance.getAllAccounts();
//
//	    if (bankAccounts != null && !bankAccounts.isEmpty()) {
//	        String basePath = context.getBaseUri().toString();
//
//	        for (BankAccount bankAccount : bankAccounts) {
//	            bankAccount.setLink(new ArrayList<Link>());
//
//	            Link linkSelf = new Link();
//	            linkSelf.setRel("self");
//	            linkSelf.setHref(basePath + "accounts");
//
//	            Link deletelink = new Link();
//	            deletelink.setRel("/linkrels/account/delete");
//	            deletelink.setHref(basePath + "accounts");
//
//	            Link updatelink = new Link();
//	            updatelink.setRel("/linkrels/account/update");
//	            updatelink.setHref(basePath + "accounts");
//
//	            bankAccount.getLink().add(linkSelf);
//	            bankAccount.getLink().add(deletelink);
//	            bankAccount.getLink().add(updatelink);
//	        }
//
//	        return Response.status(Response.Status.OK).entity(bankAccounts).build();
//	    } else {
//	        return Response.status(Response.Status.NOT_FOUND).build();
//	    }
//	}
	
	@GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<BankAccount> getBankAccounts() {
        List<BankAccount> bankAccounts = BankDaoDB.instance.getAllAccounts();

        if (bankAccounts != null && !bankAccounts.isEmpty()) {
            for (BankAccount bankAccount : bankAccounts) {
                bankAccount.setLink(new ArrayList<Link>());

                String basePath = context.getBaseUri().toString();

                Link linkSelf = new Link();
                linkSelf.setRel("self");
                linkSelf.setHref(basePath + "accounts/" + bankAccount.getBranchCode() + "/" + bankAccount.getAccountNumber());

                Link deletelink = new Link();
                deletelink.setRel("/linkrels/account/delete");
                deletelink.setHref(basePath + "accounts/" + bankAccount.getBranchCode() + "/" + bankAccount.getAccountNumber());

                Link updatelink = new Link();
                updatelink.setRel("/linkrels/account/update");
                updatelink.setHref(basePath + "accounts/" + bankAccount.getBranchCode() + "/" + bankAccount.getAccountNumber());

                bankAccount.getLink().add(linkSelf);
                bankAccount.getLink().add(deletelink);
                bankAccount.getLink().add(updatelink);
            }
            return bankAccounts;
        } else {
            return Collections.emptyList();
        }
    }

	
	
	@GET
	@Path("/{branchCode}/{accountNumber}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getAccount(@PathParam("branchCode") String branchCode, @PathParam("accountNumber") String accountNumber, @Context Request request) {
	    BankAccount bankAccount = BankDaoDB.instance.getAccountDetails(branchCode, accountNumber);
	    if (bankAccount != null) {
	    bankAccount.setLink(new ArrayList<Link>());

		String basePath = context.getBaseUri().toString();

		Link linkSelf = new Link();

		linkSelf.setRel("self");
		linkSelf.setHref(basePath + "accounts/" + branchCode + "/" + accountNumber);

		Link deletelink = new Link();
		deletelink.setRel("/linkrels/account/delete");
		deletelink.setHref(basePath + "accounts/" + branchCode + "/" + accountNumber);

		Link updatelink = new Link();
		updatelink.setRel("/linkrels/account/update");
		updatelink.setHref(basePath + "accounts/" + branchCode + "/" + accountNumber);

		bankAccount.getLink().add(linkSelf);
		bankAccount.getLink().add(deletelink);
		bankAccount.getLink().add(updatelink);

		CacheControl cc = new CacheControl();
		cc.setMaxAge(5);
		cc.setPrivate(false);
		cc.setNoStore(false);
		
		EntityTag tag = new EntityTag(Integer.toString(bankAccount.toString().hashCode()));
		
		ResponseBuilder builder = request.evaluatePreconditions(tag);
		
		if(builder != null) {
			builder.cacheControl(cc);
			builder.tag(tag);
			return builder.build();
		}
		//return Response.status(Response.Status.OK).entity(bankAccount).build();
	    
	    return Response.status(Response.Status.OK).entity(bankAccount).cacheControl(cc).tag(tag).build();
	    } else {
	        return Response.status(Response.Status.NOT_FOUND).build();
	    }
	}
	
	@POST
    @Consumes({MediaType.APPLICATION_XML})
    public Response postAccount(BankAccount bankAccount, @Context HttpServletResponse servletResponse) throws IOException {
        int accNo = BankDaoDB.instance.getNextAccountNumber(bankAccount.getBranchCode());
        bankAccount.setAccountNumber(accNo);
        int result = BankDaoDB.instance.addBankAccount(bankAccount);

        if (result == 1) {
            bankAccount.setLink(new ArrayList<Link>());

            String basePath = context.getBaseUri().toString();

            Link linkSelf = new Link();
            linkSelf.setRel("self");
            linkSelf.setHref(basePath + "accounts/" + bankAccount.getBranchCode() + "/" + bankAccount.getAccountNumber());

            Link deletelink = new Link();
            deletelink.setRel("/linkrels/account/delete");
            deletelink.setHref(basePath + "accounts/" + bankAccount.getBranchCode() + "/" + bankAccount.getAccountNumber());

            Link updatelink = new Link();
            updatelink.setRel("/linkrels/account/update");
            updatelink.setHref(basePath + "accounts/" + bankAccount.getBranchCode() + "/" + bankAccount.getAccountNumber());

            bankAccount.getLink().add(linkSelf);
            bankAccount.getLink().add(deletelink);
            bankAccount.getLink().add(updatelink);

            return Response
                    .status(Response.Status.CREATED)
                    .header("Location", String.format("%s/%s/%s/", context.getAbsolutePath().toString(),
                            bankAccount.getBranchCode(),
                            bankAccount.getAccountNumber()))
                    .entity(bankAccount)
                    .build();
        } else {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
	
	@PUT
	@Path("/{branchCode}/{accountNumber}")
	@Consumes({MediaType.APPLICATION_XML})
	public Response putAccount(@PathParam("branchCode") String branchCode,
	                           @PathParam("accountNumber") int accountNumber,
	                           BankAccount updatedBankAccount,
	                           @Context HttpServletResponse servletResponse) throws IOException {
	    BankAccount existingBankAccount = BankDaoDB.instance.getAccountDetails(branchCode, String.valueOf(accountNumber));

	    if (existingBankAccount == null) {
	        return Response.status(Response.Status.NOT_FOUND)
	                .entity("<accountDoesNotExist />")
	                .build();
	    } else if (!branchCode.equals(updatedBankAccount.getBranchCode()) || accountNumber != updatedBankAccount.getAccountNumber()) {
	        return Response.status(Response.Status.CONFLICT)
	                .entity("<conflictBetweenURIandXML />")
	                .build();
	    } else {
	        int result = BankDaoDB.instance.updateBankAccount(updatedBankAccount);

	        String basePath = context.getBaseUri().toString() + "accounts/";
		    updatedBankAccount.setLink(new ArrayList<Link>());

		    Link linkSelf = new Link();
		    linkSelf.setRel("self");
		    linkSelf.setHref(basePath + branchCode + "/" + accountNumber);
		    updatedBankAccount.getLink().add(linkSelf);

		    Link deleteLink = new Link();
		    deleteLink.setRel("/linkrels/account/delete");
		    deleteLink.setHref(basePath + branchCode + "/" + accountNumber);
		    updatedBankAccount.getLink().add(deleteLink);

		    Link updateLink = new Link();
		    updateLink.setRel("/linkrels/account/update");
		    updateLink.setHref(basePath + branchCode + "/" + accountNumber);
		    updatedBankAccount.getLink().add(updateLink);
	        if (result == 1) {
	            return Response.status(Response.Status.OK)
	                    .entity(updatedBankAccount)
	                    .build();
	        } else {
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                    .build();
	        }
	    }
	}
		
	@DELETE
	@Path("/{branchCode}/{accountNumber}")
	public Response deleteAccount(@PathParam("branchCode") String branchCode,
	                              @PathParam("accountNumber") int accountNumber,
	                              @Context HttpServletResponse servletResponse) throws IOException {
	    int result = BankDaoDB.instance.deleteBankAccount(branchCode, String.valueOf(accountNumber));

	    if (result == 1) {
	        return Response.status(Response.Status.NO_CONTENT).build();
	    } else if (result == 0) {
	        return Response.status(Response.Status.NOT_FOUND).build();
	    } else {
	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	    }
	}
	
	@HEAD
	@Path("/{branchCode}/{accountNumber}")
	public Response getAccountHead(@PathParam("branchCode") String branchCode,
	                               @PathParam("accountNumber") int accountNumber) {
	    BankAccount bankAccount = BankDaoDB.instance.getAccountDetails(branchCode, String.valueOf(accountNumber));
	    if (bankAccount == null) {
	        return Response.status(Response.Status.NOT_FOUND).build();
	    } else {
	        return Response.ok().build();
	    }
	}

	@OPTIONS
	@Path("/{branchCode}/{accountNumber}")
	public Response getAccountOptions(@PathParam("branchCode") String branchCode,
	                                  @PathParam("accountNumber") int accountNumber) {
	    BankAccount bankAccount = BankDaoDB.instance.getAccountDetails(branchCode, String.valueOf(accountNumber));
	    if (bankAccount == null) {
	        return Response.status(Response.Status.NOT_FOUND).build();
	    } else {
	        return Response.ok()
	                .header("Allow", "GET, PUT, DELETE, HEAD, OPTIONS")
	                .header("Content-Type", "application/xml")
	                .header("Content-Length", "123")
	                .build();
	    }
	}


}
