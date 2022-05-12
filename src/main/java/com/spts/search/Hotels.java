package com.spts.search;

public class Hotels {
	private int hotelId;
	private String hotelName;
	private String country;
	private String city;
	private String address;
	private int availableSingleRooms;
	private int availableDoubleRooms;
	private int availableSuites;
	private double singlePrice;
	private double doublePrice;
	public Hotels(int hotelId, String hotelName, String country, String city, String address, int availableSingleRooms,
			int availableDoubleRooms, int availableSuites, double singlePrice, double doublePrice, double suitePrice) {
		super();
		this.hotelId = hotelId;
		this.hotelName = hotelName;
		this.country = country;
		this.city = city;
		this.address = address;
		this.availableSingleRooms = availableSingleRooms;
		this.availableDoubleRooms = availableDoubleRooms;
		this.availableSuites = availableSuites;
		this.singlePrice = singlePrice;
		this.doublePrice = doublePrice;
		this.suitePrice = suitePrice;
	}
	public Hotels() {
		
	}
	private double suitePrice;
	/*private double breakfastPrice;
	private double gymPrice;
	private double poolPrice;
	private double parkingPrice;
	private double allMealsPrice;*/
	
	public int getHotelId() {
		return hotelId;
	}
	public void setHotelId(int hotelId) {
		this.hotelId = hotelId;
	}
	public String getHotelName() {
		return hotelName;
	}
	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getAvailableSingleRooms() {
		return availableSingleRooms;
	}
	public void setAvailableSingleRooms(int availableSingleRooms) {
		this.availableSingleRooms = availableSingleRooms;
	}
	public int getAvailableDoubleRooms() {
		return availableDoubleRooms;
	}
	public void setAvailableDoubleRooms(int availableDoubleRooms) {
		this.availableDoubleRooms = availableDoubleRooms;
	}
	public int getAvailableSuites() {
		return availableSuites;
	}
	public void setAvailableSuites(int availableSuites) {
		this.availableSuites = availableSuites;
	}
	public double getSinglePrice() {
		return singlePrice;
	}
	public void setSinglePrice(double singlePrice) {
		this.singlePrice = singlePrice;
	}
	public double getDoublePrice() {
		return doublePrice;
	}
	public void setDoublePrice(double doublePrice) {
		this.doublePrice = doublePrice;
	}
	public double getSuitePrice() {
		return suitePrice;
	}
	public void setSuitePrice(double suitePrice) {
		this.suitePrice = suitePrice;
	}
	/*public double getBreakfastPrice() {
		return breakfastPrice;
	}
	public void setBreakfastPrice(double breakfastPrice) {
		this.breakfastPrice = breakfastPrice;
	}
	public double getGymPrice() {
		return gymPrice;
	}
	public void setGymPrice(double gymPrice) {
		this.gymPrice = gymPrice;
	}
	public double getPoolPrice() {
		return poolPrice;
	}
	public void setPoolPrice(double poolPrice) {
		this.poolPrice = poolPrice;
	}
	public double getParkingPrice() {
		return parkingPrice;
	}
	public void setParkingPrice(double parkingPrice) {
		this.parkingPrice = parkingPrice;
	}
	public double getAllMealsPrice() {
		return allMealsPrice;
	}
	public void setAllMealsPrice(double allMealsPrice) {
		this.allMealsPrice = allMealsPrice;
	}*/
	
	
	

}
