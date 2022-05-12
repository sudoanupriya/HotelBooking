package com.spts.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.spts.misc.Login;
import com.spts.signup.User;

@RestController
public class LoginController {
	
	//private static final Logger log = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	private LoginImpl newLogin;
	@PostMapping(value = "/userLogin")
	public ResponseEntity<Object> login(@RequestBody Login currentUser, HttpServletRequest request){
		
		User loggedInUser = null;
		try {
		 loggedInUser = newLogin.userLoginCheck(currentUser.getUserName(), currentUser.getPassword());
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
		
		if (loggedInUser != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", loggedInUser);
            return ResponseEntity.ok(loggedInUser);
        } else {
            return ResponseEntity.status(401).body("Failed to login!");
        }
	}
	

}
