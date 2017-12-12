package online.skedz.scheduler.core.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import online.skedz.scheduler.core.user.Role;
import online.skedz.scheduler.core.user.User;
import online.skedz.scheduler.core.user.UserService;




@Component
public class TestUserSetup {
	
	
	@Autowired
	UserService userService;
	
	@Scheduled(fixedRate = Long.MAX_VALUE)
	public void setUpAdmin(){
   		User u = userService.create(new User().setUsername("aa@aa.aa").setPassword("asdfasdf"), Role.ROLE_ADMIN);
   		u.verifyEmail(LocalDateTime.now());
   		userService.save(u);

		System.out.println("admin account set up");
	}
}
