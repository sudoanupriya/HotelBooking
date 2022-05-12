package com.spts.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.spts.booking.Booking;

@Component
public class UpdateRoomAvailability {
	
	@Autowired
	private JdbcTemplate updateJdbcTemplate;
	public int updateRooms(Booking booking, String scenario) {	
		int hotelId = booking.getHotelId();
		int bookedSingle = booking.getSingleroomsBooked();
		int bookedDouble = booking.getDoubleroomsBooked();
		int bookedSuites = booking.getSuitesBooked();
		int bookingId = booking.getBookingId();
		// get current available rooms
		String roomQuery = "select available_single_rooms from hotels where hotel_id = ?";
		int singleCount = updateJdbcTemplate.queryForObject(roomQuery, Integer.class,hotelId);
		roomQuery = "select available_double_rooms from hotels where hotel_id = ?";
		int doubleCount = updateJdbcTemplate.queryForObject(roomQuery, Integer.class,hotelId);
		roomQuery = "select available_suites from hotels where hotel_id = ?";
		int suiteCount = updateJdbcTemplate.queryForObject(roomQuery, Integer.class,hotelId);
		// update rooms in db based on scenarios create update,delete
		if(scenario.equals("Create")) {
			singleCount = singleCount - bookedSingle;
			doubleCount = doubleCount - bookedDouble;
			suiteCount = suiteCount - bookedSuites;
		}
		else if(scenario.equals("Delete") || scenario.equals("CheckOut")) {
			singleCount = singleCount + bookedSingle;
			doubleCount = doubleCount + bookedDouble;
			suiteCount = suiteCount + bookedSuites;
		}
		else {
			//get currently booked rooms
			roomQuery = "select single_rooms_booked from booking where booking_id = ?";
			int currentsingleCount = updateJdbcTemplate.queryForObject(roomQuery, Integer.class,bookingId);
			roomQuery = "select double_rooms_booked from booking where booking_id = ?";
			int currentdoubleCount = updateJdbcTemplate.queryForObject(roomQuery, Integer.class,bookingId);
			roomQuery = "select suites_booked from booking where booking_id = ?";
			int currentsuiteCount = updateJdbcTemplate.queryForObject(roomQuery, Integer.class,bookingId);
			if(currentsingleCount<bookedSingle)
				singleCount = singleCount - (bookedSingle-currentsingleCount);
			else
				singleCount = singleCount + (currentsingleCount-bookedSingle);
			if(currentdoubleCount<bookedDouble)
				doubleCount = doubleCount - (bookedDouble-currentdoubleCount);
			else
				doubleCount = doubleCount + (currentdoubleCount-bookedDouble);
			if(currentsuiteCount<bookedSingle)
				suiteCount = suiteCount - (bookedSuites-currentsuiteCount);
			else
				suiteCount = suiteCount + (currentsuiteCount-bookedSuites);
		}
		String updateRoomQuery = "UPDATE hotels SET available_single_rooms = ?, available_double_rooms = ?, available_suites = ? where hotel_id = ?";
		int code = updateJdbcTemplate.update(updateRoomQuery,singleCount,doubleCount,suiteCount,hotelId);
		return code;
	}
	

}
