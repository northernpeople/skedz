package online.skedz.scheduler.core.schedule.repo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import online.skedz.scheduler.core.schedule.Workday;
import online.skedz.scheduler.core.user.Role;
import online.skedz.scheduler.core.user.User;
import online.skedz.scheduler.core.user.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WorkdayRepoTest {
	
	@Autowired
	WorkdayRepo wdRepo;
	
	@Autowired
	UserRepository uRepo;

	@Test
	public void shouldSaveDay(){
		User u = new User();
		u.setUsername("a@a.a");
		u.setPassword("asdfasdfsadf");
		u.setRole(Role.ROLE_ADMIN);
		u = uRepo.saveAndFlush(u);
		Workday wd = new Workday().setUser(u);
		wd = wdRepo.saveAndFlush(wd);
		wd = wdRepo.findOne(wd.getId());
		Assert.assertNotNull("should have id", wd.getId());
		Assert.assertNotNull("should have user", wd.getUser());	
	}
}
