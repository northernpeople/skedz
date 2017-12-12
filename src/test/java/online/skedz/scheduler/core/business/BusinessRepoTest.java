package online.skedz.scheduler.core.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BusinessRepoTest {

	@Autowired
	BusinessRepo repo;
	
	@Test
	public void shouldSaveBusiness(){
		Business b = new Business();
		b.setName("wankers");
		b = repo.saveAndFlush(b);
		Assert.assertNotNull("should have id", b.getId());
		Assert.assertNotNull("should save one", repo.findOne(b.getId()));

	}

}
