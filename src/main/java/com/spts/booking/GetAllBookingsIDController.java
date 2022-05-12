package com.spts.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@RestController
public class GetAllBookingsIDController {
	
	@Autowired
    private GetAllBookingsIDImpl allBokingsId;
	@GetMapping(value = "/getAllUserBookings/{userId}", produces = "application/json")
	public String greeting(@PathVariable int userId) {
		String json="";
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			json = ow.writeValueAsString(allBokingsId.getUserBookings(userId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		    return json;
		}

}
