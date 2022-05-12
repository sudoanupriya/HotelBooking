package com.spts.helper;

import org.springframework.stereotype.Component;

import com.spts.booking.Booking;

@Component
public class CheckRoomAvailability {
	
	public void checkRoomAvailability(Booking newBooking) {
		//get a list of checkout dates for all bookings with hotel id, if upcoming checkin dates are later than checkout dates fine
		// else check the count availability of rooms and required rooms are more than available, throw error not enough rooms
		
	}

}
