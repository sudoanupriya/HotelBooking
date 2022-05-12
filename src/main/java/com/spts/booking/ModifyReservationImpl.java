package com.spts.booking;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.spts.helper.CalculateFinalPrices;
import com.spts.helper.UpdateRoomAvailability;
import com.spts.interfaces.IModifyReservation;
import com.spts.signup.User;

@Component
public class ModifyReservationImpl implements IModifyReservation {
	
	@Autowired
	private JdbcTemplate modifyTemplate;
	@Autowired
	private UpdateRoomAvailability available;
	@Autowired
	private CalculateFinalPrices prices;
	
	private static final  String OUTPUTDATE = "yyyy-MM-dd";
	private static final String INPUTDATE = "MM/dd/yyyy";
	
	private static final Logger log = LoggerFactory.getLogger(ModifyReservationImpl.class);
	private double finalPrice;
	
	public double getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(double finalPrice) {
		this.finalPrice = finalPrice;
	}

	public int deleteBooking(int bookingId, User user) {
		int code = -1;
		List<Booking> booking = new ArrayList<>();
		//check if user has permission to delete
		booking = isValidBooking(bookingId);
		if(booking.isEmpty())
			return 4444;
//		if (booking.get(0).getUserId() != user.getId())
//            return 3333;
		//check if mandatory values are null
		if(booking.get(0).getUserId() == 0 || booking.get(0).getHotelId()==0 || booking.get(0).getBookingId() ==0)
			return 5555;
		//check if booking id is valid or not
		
		String deleteQuery = "update booking SET booking_status = ? WHERE booking_id = ?";
		modifyTemplate.update(deleteQuery,"Deleted",bookingId);
		deleteQuery = "select * from booking where booking_id = ?";
		List<Booking> testBooking  = modifyTemplate.query(deleteQuery, BeanPropertyRowMapper.newInstance(Booking.class),bookingId);
		available.updateRooms(booking.get(0),"Delete");
		if(testBooking.get(0).getBookingStatus().equals("Deleted"))
		 code = 1111;
		else
		 code = 2222;
		return code;
	}
	
	public int changeBooking(Booking newBooking, User user) {
		int code = -1;
		List<Booking> booking = new ArrayList<>();
		//check basic conditions
		//check if user has permission to delete
		if (newBooking.getUserId() != user.getId())
            return 6666;
		//check for number of days, can't be more than 7
		int duration = prices.checkDuration(newBooking.getCheckinDate(),newBooking.getCheckoutDate());
		if(duration>7)
			return 1111;
		// check if user email is null
		if(newBooking.getBookingEmail().equals(""))
			return 2222;
		// check if any of the mandatory are null
		if(newBooking.getUserId() == 0 || newBooking.getHotelId()==0 || newBooking.getAdultCount() ==0 || newBooking.getCheckinDate().equals("")|| newBooking.getCheckoutDate().equals("")) 
			return 3333;
		//check avialability
		//check if booking id is valid or not
		booking = isValidBooking(newBooking.getBookingId());
		if(booking.isEmpty())
			return 4444;
		//check if user id is valid or not
		boolean isValidUser = false;
		isValidUser = isValidUser(newBooking.getUserId());
		if(!isValidUser)
			return 5555;
		//calculate price
		double price = prices.calculatePrice(newBooking);
		this.setFinalPrice(price);
		//update rooms
		available.updateRooms(newBooking,"Modify");
		DateFormat inputDateFormat = new SimpleDateFormat(INPUTDATE);
		DateFormat outputDateFormat = new SimpleDateFormat(OUTPUTDATE);
		Date checkinDate  = null;
		Date checkoutDate = null;
		try {
			checkinDate = inputDateFormat.parse(newBooking.getCheckinDate());
			checkoutDate = inputDateFormat.parse(newBooking.getCheckoutDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String addNewBookingQuery = "update booking set adult_count = ?, children_count = ?, check_in_date = ?, check_out_date = ?,single_rooms_booked = ?, double_rooms_booked = ?,suites_booked = ?,final_price = ?, booking_status = ? where booking_id = ?";
		int result = modifyTemplate.update(addNewBookingQuery,newBooking.getAdultCount(),newBooking.getChildrenCount(),outputDateFormat.format(checkinDate),outputDateFormat.format(checkoutDate),newBooking.getSingleroomsBooked(),
				newBooking.getDoubleroomsBooked(),newBooking.getSuitesBooked(),price,"Upcoming",newBooking.getBookingId());
		
		if(result == 1)
			code = 1;
		else
			code = 0;
		return code;
	}
	
	private boolean isValidUser(int userId) {
		List<User> testUser = new ArrayList<>();
		String userDetailsQuery = "select * from user where id = ?";
		try {   
    	    //if user id is invalid, throw an error
			testUser = modifyTemplate.query(userDetailsQuery, BeanPropertyRowMapper.newInstance(User.class),userId);
    	    if(testUser.isEmpty())
    	    	return false;     
            
	    }
	    catch(InvalidResultSetAccessException rs) {
	    	throw new RuntimeException(rs);
	    }
	    catch(DataAccessException da) {
	    	throw new RuntimeException(da);
	    }
		return true;
	}

	public List<Booking> isValidBooking(int bookingId) {
		List<Booking> testBooking = new ArrayList<>();
		String userDetailsQuery = "select * from booking where booking_id = ?";
		try {   
    	    //if booking id is invalid, throw an error
			testBooking = modifyTemplate.query(userDetailsQuery, BeanPropertyRowMapper.newInstance(Booking.class),bookingId);    
            
	    }
	    catch(InvalidResultSetAccessException rs) {
	    	throw new RuntimeException(rs);
	    }
	    catch(DataAccessException da) {
	    	throw new RuntimeException(da);
	    }
		return testBooking;
	}

}
