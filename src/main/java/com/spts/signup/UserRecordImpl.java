package com.spts.signup;

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

import com.spts.interfaces.IUserRecord;

@Component
public class UserRecordImpl implements IUserRecord{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	private int currentUserId = -1;
	
	public int getCurrentUserId() {
		return currentUserId;
	}
	public void setCurrentUserId(int currentUserId) {
		this.currentUserId = currentUserId;
	}
	private static final Logger log = LoggerFactory.getLogger(UserRecordImpl.class);
	
	@Override
	public int addNewUserRecord(User newUser){
		int result = 0;
		List<User> testuser = new ArrayList<>();
		String encryptedPassword = getEncryptedPassword(newUser.getPassword());
        String sql = "INSERT INTO user (firstName, lastName, email, password, country, city, address, zipcode, usertype) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";
        String sql2 = "select * from user where email = ?"; 
        String sql3 = "SELECT LAST_INSERT_ID()";
        //if some of the parameters are null you can't add record 
        
        if(newUser.getFirstName().equals("") || newUser.getEmail().equals("") 
        		|| newUser.getPassword().equals("") || newUser.getZipcode().equals("") || newUser.getAddress().equals("") 
        		|| newUser.getCountry() .equals("") || newUser.getCity().equals("") || newUser.getUserType().equals("")) {
        	
        	return 2222;
        }
        try {   
        	    //if userid already exists, throw an error
        	    testuser = jdbcTemplate.query(sql2, BeanPropertyRowMapper.newInstance(User.class),newUser.getEmail());
        	    if(!testuser.isEmpty())
        	    	result = 1111;
        	    else {
	                result = jdbcTemplate.update(sql, newUser.getFirstName(), newUser.getLastName(),
	        		newUser.getEmail(),encryptedPassword,newUser.getCountry(),newUser.getCity(),newUser.getAddress(),newUser.getZipcode(), newUser.getUserType());
	                int newUserId = jdbcTemplate.queryForObject(sql3, Integer.class);
	                this.setCurrentUserId(newUserId);
	                addRewards(newUserId,100);
                }       
                
        }
        catch(InvalidResultSetAccessException rs) {
        	throw new RuntimeException(rs);
        }
        catch(DataAccessException da) {
        	throw new RuntimeException(da);
        }
         
        return result;
         
    }
	public String getEncryptedPassword(String password) {
		StringBuffer sb = new StringBuffer();
		for(char c:password.toCharArray()) {
			sb.append(c-'a');
			sb.append('%');
		}
		return sb.toString();
	}
	public void addRewards(int userId,int points) {
		int result = 0;
		int rewardPoints = points;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
	    Date date = new Date();  
	    log.info(formatter.format(date));  
		String addRewardQuery = "insert into rewards(user_id,reward_points,start_date) values(?,?,?)";
		result = jdbcTemplate.update(addRewardQuery, userId,rewardPoints,formatter.format(date));
		if(result == 1)
			log.info("new row added to rewards table");
		else
			log.info("error in adding new row to rewards table");
	}
}
