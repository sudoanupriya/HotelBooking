package com.spts.booking;

public class Booking {
	private int bookingId;
	private int userId;
	private int hotelId;
	private int adultCount;
	private int childrenCount;
	private String checkinDate;
	private String checkoutDate;
	private int singleroomsBooked;
	private int doubleroomsBooked;
	private int suitesBooked;
	private String bookingEmail;
	private String statusMessage;
	private int rewardPoints;
	private double finalPrice;
	private String bookingStatus;
	private int pool;
	private int gym;
	private int parking;
	private int meals;
	private int breakfast;
	public Booking(int bookingId, int userId, int hotelId, int adultCount, int childrenCount, String checkinDate,
			String checkoutDate, int singleroomsBooked, int doubleroomsBooked, int suitesBooked, String bookingEmail,
			String statusMessage, int rewardPoints, double finalPrice, String bookingStatus, int pool, int gym,
			int parking, int meals, int breakfast) {
		super();
		this.bookingId = bookingId;
		this.userId = userId;
		this.hotelId = hotelId;
		this.adultCount = adultCount;
		this.childrenCount = childrenCount;
		this.checkinDate = checkinDate;
		this.checkoutDate = checkoutDate;
		this.singleroomsBooked = singleroomsBooked;
		this.doubleroomsBooked = doubleroomsBooked;
		this.suitesBooked = suitesBooked;
		this.bookingEmail = bookingEmail;
		this.statusMessage = statusMessage;
		this.rewardPoints = rewardPoints;
		this.finalPrice = finalPrice;
		this.bookingStatus = bookingStatus;
		this.pool = pool;
		this.gym = gym;
		this.parking = parking;
		this.meals = meals;
		this.breakfast = breakfast;
	}
	
	
	public int getRewardPoints() {
		return rewardPoints;
	}
	public int getPool() {
		return pool;
	}
	public void setPool(int pool) {
		this.pool = pool;
	}
	public int getGym() {
		return gym;
	}
	public void setGym(int gym) {
		this.gym = gym;
	}
	public int getParking() {
		return parking;
	}
	public void setParking(int parking) {
		this.parking = parking;
	}
	public int getMeals() {
		return meals;
	}
	public void setMeals(int meals) {
		this.meals = meals;
	}
	public int getBreakfast() {
		return breakfast;
	}
	public void setBreakfast(int breakfast) {
		this.breakfast = breakfast;
	}
	public void setRewardPoints(int rewardPoints) {
		this.rewardPoints = rewardPoints;
	}
	public String getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	public Booking() {
		
	}
	
	public int getBookingId() {
		return bookingId;
	}
	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getHotelId() {
		return hotelId;
	}
	public void setHotelId(int hotelId) {
		this.hotelId = hotelId;
	}
	public int getAdultCount() {
		return adultCount;
	}
	public void setAdultCount(int adultCount) {
		this.adultCount = adultCount;
	}
	public int getChildrenCount() {
		return childrenCount;
	}
	public void setChildrenCount(int childrenCount) {
		this.childrenCount = childrenCount;
	}
	public String getCheckinDate() {
		return checkinDate;
	}
	public void setCheckinDate(String checkinDate) {
		this.checkinDate = checkinDate;
	}
	public String getCheckoutDate() {
		return checkoutDate;
	}
	public void setCheckoutDate(String checkoutDate) {
		this.checkoutDate = checkoutDate;
	}
	public int getSingleroomsBooked() {
		return singleroomsBooked;
	}
	public void setSingleroomsBooked(int singleroomsBooked) {
		this.singleroomsBooked = singleroomsBooked;
	}
	public int getDoubleroomsBooked() {
		return doubleroomsBooked;
	}
	public void setDoubleroomsBooked(int doubleroomsBooked) {
		this.doubleroomsBooked = doubleroomsBooked;
	}
	public int getSuitesBooked() {
		return suitesBooked;
	}
	public void setSuitesBooked(int suitesBooked) {
		this.suitesBooked = suitesBooked;
	}
	public String getBookingEmail() {
		return bookingEmail;
	}
	public void setBookingEmail(String bookingEmail) {
		this.bookingEmail = bookingEmail;
	}
	public double getFinalPrice() {
		return finalPrice;
	}
	public void setFinalPrice(double finalPrice) {
		this.finalPrice = finalPrice;
	}
	public String getBookingStatus() {
		return bookingStatus;
	}
	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}
	
	

}
