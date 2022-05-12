package com.spts.booking;

import com.spts.signup.User;

public class BookingUser {
    Booking newBooking;
    User user;

    public Booking getNewBooking() {
        return newBooking;
    }

    public void setNewBooking(Booking newBooking) {
        this.newBooking = newBooking;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BookingUser(Booking newBooking, User user) {
        this.newBooking = newBooking;
        this.user = user;
    }
}
