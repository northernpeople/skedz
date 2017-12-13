package online.skedz.scheduler.web.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import online.skedz.scheduler.core.user.UserService;


@Controller
@RequestMapping("/user")
public class User {
	
	@Autowired
	UserService userService;
	
	@GetMapping(value = "/main")
	public String main(Model m, Principal p){
		m.addAttribute("user_id", userService.byUserName(p.getName()).getId());
		return "user/main";	
	}
}

