package online.skedz.scheduler.core.service.scheduled;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import online.skedz.scheduler.core.business.Business;
import online.skedz.scheduler.core.business.BusinessService;
import online.skedz.scheduler.core.business.ServiceType;
import online.skedz.scheduler.core.business.repo.ServiceTypeRepo;
import online.skedz.scheduler.core.user.Role;
import online.skedz.scheduler.core.user.User;
import online.skedz.scheduler.core.user.UserService;

@Component
public class ScheduledTasks {
	
	
	@Autowired
	UserService userService;
	
	@Autowired
	BusinessService businessService;
	
	@Autowired
	ServiceTypeRepo stRepo;
	
	@Scheduled(fixedRate = Long.MAX_VALUE)
	public void setUpTestAccount(){
   		User u = userService.create(new User().setUsername("aa@aa.aa").setPassword("asdfasdf"), Role.ROLE_ADMIN);
   		u.verifyEmail(LocalDateTime.now());
   		userService.save(u);
   		
   		Business b  = businessService.create( new Business().setName("AAA towing") );
   		u = userService.addUserToBusiness(b, u);
   		
   		ServiceType type = stRepo.saveAndFlush(
				new ServiceType().setName("noodleing").setDuration(5));
   		
		b = businessService.addServiceType(b, type);
		


		System.out.println("test admin account set up");
	}
	
	@Scheduled(initialDelay = 5000, fixedRate = 60000)
	public void deleteUnverifiedUsers(){
		List<User> all = userService.findAll();
		for(User u : all){
			//if account is disabled and was created longer than a week ago from today's date
		if(!u.isEnabled() && u.getCreatedDate().plusWeeks(1L).isBefore(LocalDateTime.now())){
				System.out.println("Deleting user: " + u);
				userService.delete(u.getId());
			}
		}
	}
}
