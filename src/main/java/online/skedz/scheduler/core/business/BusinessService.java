package online.skedz.scheduler.core.business;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import online.skedz.scheduler.core.business.repo.BusinessRepo;
import online.skedz.scheduler.core.business.repo.ServiceTypeRepo;
import online.skedz.scheduler.core.user.User;

@Service
public class BusinessService {
	
	@Autowired
	BusinessRepo bRepo;
	
	@Autowired
	ServiceTypeRepo stRepo;

	public Business addServiceType(Business b, ServiceType type){
		Assert.notNull(b, "business cannot be null");
		Assert.notNull(b.getId(), "business must have persistent identity");
		Assert.notNull(type, "type cannot be null");
		Assert.notNull(type.getId(), "type must have persistent identity");
		b.getServicesProvided().add(type);
		return bRepo.saveAndFlush(b);
	}
	
	public Business deleteServiceType(Business b, ServiceType type){
		Assert.notNull(b, "business cannot be null");
		Assert.notNull(b.getId(), "business must have persistent identity");
		Assert.notNull(type, "type cannot be null");
		Assert.notNull(type.getId(), "type must have persistent identity");
		b.getServicesProvided().remove(type);
		return bRepo.saveAndFlush(b);

	}
	
	public ServiceType create(ServiceType type){
		Assert.notNull(type, "type cannot be null");
		return stRepo.saveAndFlush(type);
	}
	
	public Business create(Business b) {
		Assert.notNull(b, "business cannot be null");
		Assert.notNull(b.getName(), "business must have name");
		return bRepo.saveAndFlush(b);
	}
	
	public Business businessById(UUID bid){
		return bRepo.findOne(bid);
	}
	
	public ServiceType byId(UUID id){
		return stRepo.findOne(id);
	}
	
	public Business getOneWithTeam(UUID businessId){
		return bRepo.findOneWithTeam(businessId);
	}

}
