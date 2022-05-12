package com.spts.misc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.spts.signup.User;

@Component
public class GetRewardsImpl {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public Rewards getRewards(int userId) {
		List<User> testUser = new ArrayList<>();
		List<Rewards> testReward = new ArrayList<>();
		String validateUser = "select * from user where id = ?";
		String rewardQuery = "select * from rewards where user_id = ?";
		
		try {   
    	    //if userid is not valid throw an error
			testUser = jdbcTemplate.query(validateUser, BeanPropertyRowMapper.newInstance(User.class),userId);
			//if user id is invalid return null rewards
    	    if(testUser.isEmpty())
    	    	return new Rewards();
    	    else {
    	    	testReward = jdbcTemplate.query(rewardQuery, BeanPropertyRowMapper.newInstance(Rewards.class),userId);
    	    	if(testReward.isEmpty())
    	    		return new Rewards();
            }       
            
    }
    catch(InvalidResultSetAccessException rs) {
    	throw new RuntimeException(rs);
    }
    catch(DataAccessException da) {
    	throw new RuntimeException(da);
    }
		return testReward.get(0);
	}

}
