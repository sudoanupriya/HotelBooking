package com.spts.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.spts.booking.Booking;
import java.text.DateFormat;

@Component
public class CalculateFinalPrices {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private static final  String DATEFORMAT = "yyyy-MM-dd";
	private static final  String SQLDATEFORMAT = "yyyy-MM-dd";
	private List<Double> roomBasePrices = new ArrayList<>();
	private List<Double> amenityPrices = new ArrayList<>();
	private int frPoints;
	
	public int getFrPoints() {
		return frPoints;
	}

	public void setFrPoints(int frPoints) {
		this.frPoints = frPoints;
	}
	private static final Logger log = LoggerFactory.getLogger(CalculateFinalPrices.class);
	
	public double calculatePrice(Booking newBooking) {
		double discountedPrice = 0;
		int earnedLoyaltyDiscountPercentage = 0;
		int earnedLoyaltyPoints = 0;
		float seasonalPricePercentage = 0;
		double tempPrice = 0;
		double afterAmenities = 0;
		
		// get base room price from db
		setRoomBasePrices(newBooking);
		setAmenityPrices(newBooking);
		tempPrice = (newBooking.getSingleroomsBooked()*roomBasePrices.get(0)) + (newBooking.getDoubleroomsBooked()*roomBasePrices.get(1)) + (newBooking.getSuitesBooked()*roomBasePrices.get(2));
		afterAmenities = (newBooking.getBreakfast()*amenityPrices.get(0) + newBooking.getGym()*amenityPrices.get(1) + newBooking.getPool()*amenityPrices.get(2) + newBooking.getParking()*amenityPrices.get(3) + newBooking.getMeals()*amenityPrices.get(4));
		//check for seasons and other things
		log.info("amenity prices "+afterAmenities);
		tempPrice+=afterAmenities;
		seasonalPricePercentage = checkForSeasonsandHolidays(newBooking.getCheckinDate(),newBooking.getCheckoutDate());
		discountedPrice = tempPrice+(tempPrice*(seasonalPricePercentage/100));
		// consider customer loyalty
		earnedLoyaltyDiscountPercentage = loyaltyDiscount(newBooking);
		discountedPrice = discountedPrice - (discountedPrice*((double)earnedLoyaltyDiscountPercentage/100));
		// calculate price
		// add some rewards to reward table based on final price of booking
		earnedLoyaltyPoints = (int) (discountedPrice/50);
		updateRewards(newBooking.getUserId(),earnedLoyaltyPoints);
		return discountedPrice;
	}
	
	public int loyaltyDiscount(Booking newBooking) {
		
		int discount = -1;
		String startDate ="";
		int rewardPoints = 0;
		SimpleDateFormat formatter = new SimpleDateFormat(DATEFORMAT);  
	    Date date = new Date();  
		String startDateQuery = "select start_date from rewards where user_id = ?";
		String rewardPointQuery = "select reward_points from rewards where user_id = ?";
		startDate = jdbcTemplate.queryForObject(startDateQuery, String.class,newBooking.getUserId());
		rewardPoints = jdbcTemplate.queryForObject(rewardPointQuery, Integer.class,newBooking.getUserId());
		int loyaltyDays = checkDuration(startDate,formatter.format(date));
			if(rewardPoints > 1000)
			discount = 7;
			else if(rewardPoints>=500 && rewardPoints<1000)
			discount = 5;
			else if(rewardPoints>=100 && rewardPoints<500)
			discount = 4;
			else
			discount = 1;
		return discount;
	}
	
	public void setRoomBasePrices(Booking newBooking) {
		String basePriceQuery = "select single_price from hotels where hotel_id = ?";
		Double singlePrice = jdbcTemplate.queryForObject(basePriceQuery, Double.class,newBooking.getHotelId());
		roomBasePrices.add(singlePrice);
		basePriceQuery = "select double_price from hotels where hotel_id = ?";
		Double doublePrice = jdbcTemplate.queryForObject(basePriceQuery, Double.class,newBooking.getHotelId());
		roomBasePrices.add(doublePrice);
		basePriceQuery = "select suite_price from hotels where hotel_id = ?";
		Double suitePrice = jdbcTemplate.queryForObject(basePriceQuery, Double.class,newBooking.getHotelId());
		roomBasePrices.add(suitePrice);
	}
	public void setAmenityPrices(Booking newBooking) {
		String basePriceQuery = "select daily_con_bf from hotels where hotel_id = ?";
		Double breakfast = jdbcTemplate.queryForObject(basePriceQuery, Double.class,newBooking.getHotelId());
		amenityPrices.add(breakfast);
		basePriceQuery = "select gym from hotels where hotel_id = ?";
		Double gym = jdbcTemplate.queryForObject(basePriceQuery, Double.class,newBooking.getHotelId());
		amenityPrices.add(gym);
		basePriceQuery = "select pool from hotels where hotel_id = ?";
		Double pool = jdbcTemplate.queryForObject(basePriceQuery, Double.class,newBooking.getHotelId());
		amenityPrices.add(pool);
		basePriceQuery = "select parking from hotels where hotel_id = ?";
		Double parking = jdbcTemplate.queryForObject(basePriceQuery, Double.class,newBooking.getHotelId());
		amenityPrices.add(parking);
		basePriceQuery = "select meals from hotels where hotel_id = ?";
		Double meals = jdbcTemplate.queryForObject(basePriceQuery, Double.class,newBooking.getHotelId());
		amenityPrices.add(meals);
	}
	
	/*
	 * taken from https://www.javaprogramto.com/2020/11/calculate-days-between-two-dates-in.html#:~:text=getTime()%20method%20for%20two,That's%20all.
	 */
	public int checkDuration(String checkinDate, String checkoutDate) {
		SimpleDateFormat formatter = new SimpleDateFormat(DATEFORMAT); 
		SimpleDateFormat sqlFormatter = new SimpleDateFormat(SQLDATEFORMAT);
		int daysDiff = -1;
		Date d1 = null, d2 = null;
		try {
			d2 = formatter.parse(checkoutDate);
			if(checkinDate.contains("/"))
				d1 = formatter.parse(checkinDate);
			else
			    d1 = sqlFormatter.parse(checkinDate);
			// getting milliseconds for both dates
			long date2InMs = d1.getTime();
			long date1InMs = d2.getTime();
			
			// getting the diff between two dates.
			long timeDiff = 0;
			if(date1InMs > date2InMs) {
				timeDiff = date1InMs - date2InMs;
			} else {
				timeDiff = date2InMs - date1InMs;
			}
			
			// converting diff into days
			daysDiff = (int) (timeDiff / (1000 * 60 * 60* 24));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return daysDiff;
	}
	
	public float checkForSeasonsandHolidays(String checkInDate, String checkOutDate) {
		
		float tempPrice = 0;
		boolean isWeekend = false;
		Random random = new Random();
		DateFormat formatter = new SimpleDateFormat(DATEFORMAT);
		Date endDateObject = null;
		Date startDateObject = null;
		try {
			 endDateObject = formatter.parse(checkOutDate);
			 startDateObject = formatter.parse(checkInDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		int startDate = startDateObject.getDate();
	    int endDate = endDateObject.getDate();
	    int startMonth = startDateObject.getMonth();
		//check summer
		if(startMonth == 5 || startMonth == 6 || startMonth ==7)
			tempPrice = tempPrice + (20+random.nextInt(10));
		//check winter and xmas
		if(startMonth == 12 || (startDate>=20 && endDate<=31))
			tempPrice = tempPrice + (20+random.nextInt(10));
		//check for new year
		if(startMonth == 1 || (startDate>=1 && endDate<=10))
			tempPrice = tempPrice + (20+random.nextInt(10));
        // check for thanks giving
		if((startMonth == 11) && (startDate>=20 && endDate<=30))
			tempPrice = tempPrice + (20+random.nextInt(10));
		//check for july4
		if((startMonth == 7) && (startDate>=1 && endDate<=7))
			tempPrice = tempPrice + (10+random.nextInt(10));
		//check for weekend if not startdate is friday sat or sun and end date is sun, mon 
		// 1-sunday, 2-tuesday
		Date date1 = null,date2 = null;
		try {
			date1 = formatter.parse(checkInDate);
			date2 = formatter.parse(checkOutDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		int dd1 =  cal.get(Calendar.DAY_OF_WEEK);
		cal.setTime(date2);
		int dd2 =  cal.get(Calendar.DAY_OF_WEEK);
		if(dd1 == 6 || dd1 == 7 || dd1 == 1 || dd2 == 1 || dd2 == 2)
			isWeekend = true;
	    if(isWeekend)
	    	tempPrice = tempPrice + (5+random.nextInt(10));
	    else
	    	tempPrice = tempPrice + 0;
		return tempPrice;
	}
	public void updateRewards(int userId,int extraPoints) {
		int result = 0;
		String getPointsQuery = "select reward_points from rewards where user_id = ?";
		int currentPoints = jdbcTemplate.queryForObject(getPointsQuery, Integer.class,userId);
		int updatedPoints = currentPoints+extraPoints;
		this.setFrPoints(updatedPoints);
		String updateRewardQuery = "update rewards set reward_points = ? where user_id = ?";
		result = jdbcTemplate.update(updateRewardQuery, updatedPoints,userId);
		if(result == 1)
			log.info("reward points modified");
		else
			log.info("problem in modifying reward points");
	}

}
