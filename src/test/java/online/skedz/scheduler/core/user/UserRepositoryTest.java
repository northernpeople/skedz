package online.skedz.scheduler.core.user;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {
	
	@Autowired
	UserRepository repo;
	
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

}
