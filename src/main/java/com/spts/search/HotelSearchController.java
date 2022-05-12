package com.spts.search;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HotelSearchController {
	
	@Autowired
	HotelSearchImpl hotelSearch;
	private static final Logger log = LoggerFactory.getLogger(HotelSearchController.class);
	@GetMapping(value = "/searchHotels", produces = "application/json")
	public List<Hotels> getHotels(@RequestParam(value = "searchkey") String searchkey) {
		List<Hotels> searchResults = new ArrayList<Hotels>();
		searchResults = hotelSearch.searchHotels(searchkey);
		return searchResults;
	}

}
