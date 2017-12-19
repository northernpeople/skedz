package online.skedz.scheduler.core.user;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import online.skedz.scheduler.core.business.ServiceType;
import online.skedz.scheduler.core.business.repo.ServiceTypeRepo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {
	
	@Autowired
	UserRepository repo;
	
	@Autowired
	ServiceTypeRepo stRepo;
	
	@Autowired
	UserService uService;
	
	
	@Test
	public void shouldSaveUser(){
		User u = new User();
		u.setUsername("a@a.a");
		u.setPassword("asdfasdfsadf");
		u.setRole(Role.ROLE_ADMIN);
		u = repo.saveAndFlush(u);
		Assert.assertNotNull("should have Id", u.getId());
		Assert.assertEquals("a@a.a", u.getUsername());
		Assert.assertEquals(u.getRole(), Role.ROLE_ADMIN);
		Assert.assertEquals("asdfasdfsadf", u.getPassword());
	}

	@Test
	public void shouldFindUsersByServiceType(){
		User u = new User();
		u.setUsername("a@a.a");
		u.setPassword("asdfasdfsadf");
		u.setRole(Role.ROLE_ADMIN);
		u = repo.saveAndFlush(u);
		
		User u2 = new User();
		u2.setUsername("a@b.c");
		u2.setPassword("asdfasdfsadf");
		u2.setRole(Role.ROLE_USER);
		u2 = repo.saveAndFlush(u2);
		
		ServiceType type = stRepo.saveAndFlush(
				new ServiceType().setName("noodleing").setDuration(5));
		
		uService.addServiceTypeTo(u, type);
		uService.addServiceTypeTo(u2, type);
		
		Assert.assertTrue("should contain both", repo.findByServicesProvidedId(type.getId()).size() == 2);
	}
}
