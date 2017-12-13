package online.skedz.scheduler.core.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import online.skedz.scheduler.core.business.repo.BusinessRepo;
import online.skedz.scheduler.core.business.repo.ServiceTypeRepo;
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
	ServiceTypeRepo stRepo;
	
	@Autowired
	UserRepository uRepo;
	
	@Autowired
	BusinessService service;
	
	ServiceType type;
	Business b;
	
	@Before
	public void init(){
		b = bRepo.saveAndFlush(
				new Business().setName("wankers"));
		type = stRepo.saveAndFlush(
				new ServiceType().setName("noodleing").setDuration(5));
	}
	
	@Test
	public void shouldAddServiceType(){
		b = service.addServiceType(b, type);
		b = bRepo.findOne(b.getId());
		Assert.assertTrue(b.getServicesProvided().contains(type));
	}
	

}
