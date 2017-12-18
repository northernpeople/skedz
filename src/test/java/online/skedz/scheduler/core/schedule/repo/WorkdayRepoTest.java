package online.skedz.scheduler.core.schedule.repo;

import org.junit.Assert;
import org.junit.Before;
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
	
	User u;
	
	@Before
	public void init(){
		u = new User();
		u.setUsername("a@a.a");
		u.setPassword("asdfasdfsadf");
		u.setRole(Role.ROLE_ADMIN);
		u = uRepo.saveAndFlush(u);
	}

	@Test
	public void shouldSaveDay(){
		Workday wd = new Workday().setUser(u);
		wd = wdRepo.saveAndFlush(wd);
		wd = wdRepo.findOne(wd.getId());
		Assert.assertNotNull("should have id", wd.getId());
		Assert.assertNotNull("should have user", wd.getUser());	
	}
	
	
	@Test
	public void shouldDeleteDay(){
		Workday wd = new Workday().setUser(u);
		wd = wdRepo.saveAndFlush(wd);
		wd = wdRepo.findOne(wd.getId());
		Assert.assertNotNull("should have id", wd.getId());
		Assert.assertNotNull("should have user", wd.getUser());	
		wdRepo.delete(wd);
		Assert.assertNull(wdRepo.findOne(wd.getId()));
	}
	
	
	@Test
	public void shouldFindAllDays(){
		Workday wd = new Workday().setUser(u);
		wd = wdRepo.saveAndFlush(wd);
		Workday wd2 = new Workday().setUser(u);
		wd2 = wdRepo.saveAndFlush(wd);
		wdRepo.findAllByUser(u);
		Assert.assertTrue(wdRepo.findAllByUser(u).contains(wd));
		Assert.assertTrue(wdRepo.findAllByUser(u).contains(wd2));
	}
}
