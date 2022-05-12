package com.spts.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogOutController {
	
	@PostMapping(value = "/logout")
    public String logout(HttpServletRequest request) throws Exception {
        if (request.getSession().getAttribute("user") != null) request.getSession().removeAttribute("user");
        return "Log out success";
    }

}
