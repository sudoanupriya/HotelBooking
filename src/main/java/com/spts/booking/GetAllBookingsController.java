package com.spts.booking;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetAllBookingsController {

    @Autowired
    private GetAllBookingsImpl allBookings;

    @GetMapping(value = "/getAllBookings", produces = "application/json")
    public String getBookings(){
        String json="";
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            json = ow.writeValueAsString(allBookings.getUserBookings());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }


}
