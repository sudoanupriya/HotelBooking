package com.spts.booking;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.spts.helper.CalculateFinalPrices;
import com.spts.signup.User;


@RestController
public class CreateBookingController {
	
	@Autowired
	CreateBookingImpl createBooking;
	
	@Autowired
	CalculateFinalPrices rewards;
	
	private final String notLoggedIn = "Must be logged in.";
	
	@PostMapping(value = "/createBooking", consumes = "application/json", produces = "application/json")
	public Booking createNewBooking(@RequestBody BookingUser booking, HttpServletRequest request){
	   User user = booking.getUser();
	   Booking newBooking = booking.getNewBooking();
       if (user == null) {
    	   newBooking.setStatusMessage(notLoggedIn);
    	   return newBooking;
       }

	   String json="";
	   ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
	   int result = createBooking.makeNewReservation(newBooking,user);
	   switch(result) {
	   case 0 : newBooking.setStatusMessage("Something went wrong while booking..Please try again");
	   break;
	   case 1 : newBooking.setStatusMessage("Booking successful.");
	   newBooking.setFinalPrice(createBooking.getFinalPrice());
	   newBooking.setBookingId(createBooking.getCurrentBookingId());
	   newBooking.setRewardPoints(rewards.getFrPoints());
	   break;
	   case 1111 : newBooking.setStatusMessage("Stay duration is more than 7 days, can't proceed with booking");
	   break;
	   case 2222 : newBooking.setStatusMessage("Booking email can't be null");
	   break;
	   case 3333 : newBooking.setStatusMessage("One of the mandatory values is null, please correct");
	   break;
	   case 4444 : newBooking.setStatusMessage("No user record exists with provided user id, Please check");
	   break;
	   case 5555 : newBooking.setStatusMessage("No rooms available on your selected dates, please try different dates");
	   break;
	   case 6666 : newBooking.setStatusMessage("Invalid hotel id");
	   break;
	   case 7777 : newBooking.setStatusMessage("Permission Denied");
	   break;
	   case 8888 : newBooking.setStatusMessage("Booking email does not match");
	   break;
	   case 9999 : newBooking.setStatusMessage("Invalid date formats");
	   break;
	   default: newBooking.setStatusMessage("Unknown error");
	   }
	   try {
		json = ow.writeValueAsString(newBooking);
	} catch (JsonProcessingException e) {
		e.printStackTrace();
	}
	    return newBooking;
	
  }

}
