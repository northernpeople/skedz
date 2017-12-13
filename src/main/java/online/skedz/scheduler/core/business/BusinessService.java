package online.skedz.scheduler.core.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import online.skedz.scheduler.core.user.User;

@Service
public class BusinessService {
	
	@Autowired
	BusinessRepo bRepo;

	public Business create(Business b) {
		Assert.notNull(b, "business cannot be null");
		Assert.notNull(b.getName(), "business must have name");
		return bRepo.saveAndFlush(b);
	}

}
