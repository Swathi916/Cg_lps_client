package com.capgemini.lps.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.lps.entity.Applicant;
import com.capgemini.lps.entity.Loan;
import com.capgemini.lps.entity.User;
import com.capgemini.lps.exception.EmailNotFoundException;
import com.capgemini.lps.exception.UserNotFoundException;
import com.capgemini.lps.response.Response;
import com.capgemini.lps.service.ApplicantService;
import com.capgemini.lps.service.LoanService;
import com.capgemini.lps.service.UserService;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/api2")
public class ApprovalRestController {
	
	@Autowired
	private LoanService loanService;
	@Autowired
	private UserService userService;
	@Autowired
	private ApplicantService applicantService;
	
	@GetMapping("/getLoansByPage/{pageNo}/{itemsPerPage}")
	public Page<Loan> getLoans(@PathVariable int pageNo ,@PathVariable int itemsPerPage){ 
	return loanService.getLoans(pageNo, itemsPerPage);
	}
	
	
	@GetMapping("/getSortedLoans/{pageNo}/{itemsPerPage}/{fieldName}")
	public Page<Loan> getSortLoans(@PathVariable int pageNo,@PathVariable int itemsPerPage,@PathVariable String fieldName) {
		return loanService.getSortLoans(pageNo, itemsPerPage, fieldName);
	}
	@GetMapping("/getRequested/{pageNo}/{itemsPerPage}")
	public Page<User> getAllRequested(@PathVariable int pageNo ,@PathVariable int itemsPerPage){ 
		return userService.getAllRequested(pageNo, itemsPerPage);
	}
	
	@GetMapping("/getSortRequested/{pageNo}/{itemsPerPage}/{fieldName}")
	public Page<User> getAllSortRequested(@PathVariable int pageNo,@PathVariable int itemsPerPage,@PathVariable String fieldName) {
		return userService.getAllSortedRequested(pageNo, itemsPerPage, fieldName);
	}

	@GetMapping("/getCustomer/{pageNo}/{itemsPerPage}")
	public Page<User> getAllCustomer(@PathVariable int pageNo ,@PathVariable int itemsPerPage){ 
		return userService.findAllCustomer(pageNo, itemsPerPage);
	}
	@GetMapping("/getSortCustomer/{pageNo}/{itemsPerPage}/{fieldName}")
	public Page<User> getAllSortCustomer(@PathVariable int pageNo,@PathVariable int itemsPerPage,@PathVariable String fieldName) {
		return userService.findAllSortedCustomer(pageNo, itemsPerPage, fieldName);
	}
	
	@PutMapping("/loanApplicationApprove/{appId}")
	public Response<Applicant> findAllApproved(@PathVariable Integer appId) {
		Applicant result = applicantService.setApproved(appId);
		if (result == null) {
			return new Response<>(true, "Updation failed", null);
		} else {
			return new Response<>(false, "successfully updated", result);
		}

	}

	@PutMapping("/loanApplicationReject/{appId}")
	public Response<Applicant> findAllRejected(@PathVariable Integer appId) {
		Applicant result = applicantService.setRejected(appId);
		if (result == null) {
			return new Response<>(true, "Updation failed", null);
		} else {
			return new Response<>(false, "successfully updated", result);
		}
	}
	
	@GetMapping("getUserById/{userId}")
	public Response<User> getClient(@PathVariable int userId) {

		User theUser = userService.findById(userId);

		if (theUser != null) {
			return new Response<>(false, "records found", theUser);
		} else {
			throw new UserNotFoundException("record not found");
		}

	}
	@PutMapping("/changePassword/{email}/{password}")
	public Response<User> changePassword(@PathVariable String email,@PathVariable String password ){
		User user= userService.getByEmail(email);
		if(user!=null) {
			user.setPassword(password);
			userService.save(user);
			return new Response<User>(false,"Password changed successfully",user);
		}
		else {
			return new Response<User>(true,"user not found",null);
		}
	}

//	@PutMapping("/updateUser")
//	public Response<User> updateUser(@Valid @RequestBody User theUser) {
//		
//		User user1 = userService.save1(theUser);
//		if(user1!=null) {
//			return new Response<>(false,"User updated successfully",user1);
//		}
//		else {
//			return new Response<>(true,"updation failed",null);
//		}
//	}
//	
	
	@PutMapping("/updateUser")
	public Response<User> updateUser( @RequestBody User user) {
	    	
		User user2 = userService.findById(user.getUserId());
		if(user2==null) {
			throw new UserNotFoundException("user not found...!!");
		}
		else {
			user.setPassword(user2.getPassword());
			userService.save1(user);
			return new Response<User>(false,"updated successfully",user);
		}
	}
	@PutMapping("/forgotpwd/{email}/{password}")
	public Response<User> forgotPassword(@PathVariable String email,@PathVariable String password , @RequestBody User theUser) {
		
theUser= userService.findByEmail(email);
		
		
		if (theUser!= null) {
			theUser.setPassword(password);
			userService.save(theUser);
			return new Response<>(false, "successfully saved", theUser);
       
		} else {
			throw new EmailNotFoundException("Email does not exist");
		}
	
	
	}        

	

}
