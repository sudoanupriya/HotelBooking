package com.spts.booking;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.spts.signup.User;

@Component
public class GetAllBookingsIDImpl {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	public List<Booking> getUserBookings(int userId){
		List<User> testuser = new ArrayList<>();
		String userDetailsQuery = "select * from user where id = ?"; 
		try {   
    	    //if invalid, throw an error
    	    testuser = jdbcTemplate.query(userDetailsQuery, BeanPropertyRowMapper.newInstance(User.class),userId);
    	    //if booking list is empty, then user id is invalid
    	    if(testuser.isEmpty())
    	    	return new ArrayList<Booking>();     
            
	    }
	    catch(InvalidResultSetAccessException rs) {
	    	throw new RuntimeException(rs);
	    }
	    catch(DataAccessException da) {
	    	throw new RuntimeException(da);
	    }
		String sql = "select booking_id as bookingId, user_id as userId, hotel_id as hotelId, adult_count as adultCount, children_count as childrenCount, check_in_date as checkinDate, "
				+ "check_out_date as checkoutDate, single_rooms_booked as singleroomsBooked, double_rooms_booked as doubleroomsBooked, suites_booked as suitesBooked, "
				+ "booking_email as bookingEmail, final_price as finalPrice, booking_status as bookingStatus from booking where user_id = ?";
		List<Booking> bookingList = new ArrayList<>();
		try {
			bookingList = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Booking.class),userId); 
		}
		catch(DataAccessException da) {
	        throw new RuntimeException(da);
	    }
		return bookingList;
	}
}
