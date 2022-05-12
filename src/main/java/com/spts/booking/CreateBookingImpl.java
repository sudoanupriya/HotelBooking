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
import com.spts.helper.CheckRoomAvailability;
import com.spts.helper.UpdateRoomAvailability;
import com.spts.interfaces.ICreateBooking;
import com.spts.search.Hotels;
import com.spts.signup.User;

@Component
public class CreateBookingImpl implements ICreateBooking {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private CalculateFinalPrices prices;
	@Autowired
	private UpdateRoomAvailability update;
	@Autowired 
	private CheckRoomAvailability available;
	
	private static final Logger log = LoggerFactory.getLogger(CreateBookingImpl.class);
	
	
	
	private int currentBookingId;
	
	private static final  String OUTPUTDATE = "yyyy-MM-dd";
	private static final String INPUTDATE = "yyyy-MM-dd";
	
	private double finalPrice = 0;
	
	public double getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(double finalPrice) {
		this.finalPrice = finalPrice;
	}

	private boolean roomsNotAvailable = false;
	
	public int makeNewReservation(Booking newBooking,User user) {
		List<User> testuser = new ArrayList<>();
		List<Hotels> testhotel = new ArrayList<>();
		String userDetailsQuery = "select * from user where id = ?"; 
		String hotelDetailsQuery = "select * from hotels where hotel_id = ?";
		String currentBookingIdQuery = "SELECT LAST_INSERT_ID()";
		
		if(user.getId() != newBooking.getUserId())
			return 7777;
		if(!user.getEmail().equals(newBooking.getBookingEmail()))
			return 8888;
		int code = -1;
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
		//check if the provided user id is valid or not
		try {   
    	    //if userid is not valid, throw an error
    	    testuser = jdbcTemplate.query(userDetailsQuery, BeanPropertyRowMapper.newInstance(User.class),newBooking.getUserId());
    	    if(testuser.isEmpty())
    	    	return 4444;     
            
	    }
	    catch(InvalidResultSetAccessException rs) {
	    	throw new RuntimeException(rs);
	    }
	    catch(DataAccessException da) {
	    	throw new RuntimeException(da);
	    }
		//check if selected hotel is valid?
		try {   
    	    //if hotelid is not valid, throw an error
			testhotel = jdbcTemplate.query(hotelDetailsQuery, BeanPropertyRowMapper.newInstance(Hotels.class),newBooking.getHotelId());
    	    if(testhotel.isEmpty())
    	    	return 6666;     
            
	    }
	    catch(InvalidResultSetAccessException rs) {
	    	throw new RuntimeException(rs);
	    }
	    catch(DataAccessException da) {
	    	throw new RuntimeException(da);
	    }
		//check availability
		available.checkRoomAvailability(newBooking);
		if(roomsNotAvailable)
			return 5555;
		//add new row in db
		finalPrice = prices.calculatePrice(newBooking);
		DateFormat inputDateFormat = new SimpleDateFormat(INPUTDATE);
		DateFormat outputDateFormat = new SimpleDateFormat(OUTPUTDATE);
		Date checkinDate  = null;
		Date checkoutDate = null;
		try {
			checkinDate = inputDateFormat.parse(newBooking.getCheckinDate());
			checkoutDate = inputDateFormat.parse(newBooking.getCheckoutDate());
		} catch (ParseException e) {
			code = 9999;
			return code;
		}
		
		String addNewBookingQuery = "INSERT INTO booking (user_id, hotel_id, booking_email, adult_count, children_count, check_in_date, check_out_date, "
				+ "single_rooms_booked, double_rooms_booked,suites_booked,final_price,booking_status,daily_con_bf,gym,pool,parking,meals) VALUES (?,?,?,?,?,?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?)";
		code = jdbcTemplate.update(addNewBookingQuery, newBooking.getUserId(), newBooking.getHotelId(),
				newBooking.getBookingEmail(),newBooking.getAdultCount(),newBooking.getChildrenCount(),outputDateFormat.format(checkinDate),outputDateFormat.format(checkoutDate),newBooking.getSingleroomsBooked(),
				newBooking.getDoubleroomsBooked(),newBooking.getSuitesBooked(),finalPrice,"Upcoming",newBooking.getBreakfast(),newBooking.getGym(),newBooking.getPool(),newBooking.getParking(),newBooking.getMeals());
		//return booking id
		int newBookingId = jdbcTemplate.queryForObject(currentBookingIdQuery, Integer.class);
        this.setCurrentBookingId(newBookingId);
        update.updateRooms(newBooking,"Create");
		return code;
	}
	
	public int getCurrentBookingId() {
		return currentBookingId;
	}

	public void setCurrentBookingId(int currentBookingId) {
		this.currentBookingId = currentBookingId;
	}

}
