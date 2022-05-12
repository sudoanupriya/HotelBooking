package com.spts.misc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.spts.signup.UserRecordImpl;

@RestController
public class GetRewardsController {
	
	@Autowired
	private GetRewardsImpl rewards;
	@GetMapping(value = "/getRewards/{userId}", produces = "application/json")
	public Rewards getRewards(@PathVariable int userId) {
		Rewards json=null;
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			json = rewards.getRewards(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		    return json;
		}

}
