package online.skedz.scheduler.core.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import online.skedz.scheduler.core.user.User;

@Service
public class BusinessService {
	
	@Autowired
	BusinessRepo bRepo;

	public Business addUser(Business b, User u){
		Assert.notNull(b, "business cannot be null");
		Assert.notNull(u, "user cannot be null");
		Assert.notNull(b.getId(), "business must have persistent id");
		Assert.notNull(u.getId(), "user must have persistent id");
		b.getTeam().add(u);
		return bRepo.saveAndFlush(b);
	}

}
