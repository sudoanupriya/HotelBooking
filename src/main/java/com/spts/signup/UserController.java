package com.spts.signup;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;


@RestController
public class UserController{

	
	//private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserRecordImpl userOperations;
	
   @PostMapping(value = "/createUser", consumes = "application/json", produces = "application/json")
	public String createPerson(@RequestBody User newUser){
	   String json="";
	   ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
	   int result = userOperations.addNewUserRecord(newUser);
	   switch(result) {
	   case 0 : json = "Something went wrong while creating your account..Please try again";
	   break;
	   case 1 : json = "Registration successful. Congratulations! your unique user id is-"+userOperations.getCurrentUserId();
	   break;
	   case 1111 : json = "Record with this user id already exists";
	   break;
	   case 2222 : json = "One of the mandatory values is empty.. please correct";
	   break;
	   default: json = "Unknown error";
	   }
	   try {
		json = ow.writeValueAsString(json);
	} catch (JsonProcessingException e) {
		e.printStackTrace();
	}
	    return json;
	
  }
}
