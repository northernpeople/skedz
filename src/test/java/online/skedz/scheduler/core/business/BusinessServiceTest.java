package online.skedz.scheduler.core.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import online.skedz.scheduler.core.user.Role;
import online.skedz.scheduler.core.user.User;
import online.skedz.scheduler.core.user.UserRepository;

import org.junit.Assert;
import org.junit.Before;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BusinessServiceTest {

	@Autowired
	BusinessRepo bRepo;
	
	@Autowired
	UserRepository uRepo;
	
	@Autowired
	BusinessService service;
	
	Business b;
	User u;
	
	@Before
	public void init(){
		b = new Business();
		b.setName("wankers");
		b = bRepo.saveAndFlush(b);
		u = new User();
		u.setUsername("a@a.a");
		u.setPassword("asdfasdfsadf");
		u.setRole(Role.ROLE_ADMIN);
		u = uRepo.saveAndFlush(u);
	}
	@Test
	public void shouldAddUserToBusiness(){
		b = service.addUserToBusiness(b, u);
		b = bRepo.findOneWithTeam(b.getId());
		Assert.assertTrue("should contain user", b.getTeam().contains(u));;
	}

}
