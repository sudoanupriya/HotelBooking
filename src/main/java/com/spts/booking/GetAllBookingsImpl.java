package com.spts.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GetAllBookingsImpl {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Booking> getUserBookings() {

        String sql = "select booking_id as bookingId, user_id as userId, hotel_id as hotelId, adult_count as adultCount, children_count as childrenCount, check_in_date as checkinDate, "
                + "check_out_date as checkoutDate, single_rooms_booked as singleroomsBooked, double_rooms_booked as doubleroomsBooked, suites_booked as suitesBooked, "
                + "booking_email as bookingEmail, final_price as finalPrice, booking_status as bookingStatus from booking";
        List<Booking> bookingList = new ArrayList<>();
        try {
            bookingList = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Booking.class));
        }catch(DataAccessException da) {
            throw new RuntimeException(da);
        }
        return bookingList;

    }
}
