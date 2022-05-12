package com.spts.search;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class HotelSearchImpl {
	
	@Autowired
	JdbcTemplate searchTemplate;
	//private static final Logger log = LoggerFactory.getLogger(HotelSearchImpl.class);
	public List<Hotels> searchHotels(String key) {
		String sql = "select * from hotels where country = ? OR hotel_name LIKE '%"+key+"%'";  
		List<Hotels> results = new ArrayList<>();
		try {
		results = searchTemplate.query(sql, BeanPropertyRowMapper.newInstance(Hotels.class),key);
		}
		catch(InvalidResultSetAccessException rs) {
        	throw new RuntimeException(rs);
        }
        catch(DataAccessException da) {
        	throw new RuntimeException(da);
        }
		return results;
	}

}
