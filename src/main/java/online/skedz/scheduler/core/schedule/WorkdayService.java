package online.skedz.scheduler.core.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import online.skedz.scheduler.core.schedule.repo.WorkdayRepo;

@Service
public class WorkdayService {
	
	@Autowired
	WorkdayRepo wdRepo;
	
	public Workday createWorkday(Workday day){
		Assert.notNull(day, "workday cannot be null");
		Assert.notNull(day.getUser(), "workday must have user");
		Assert.notNull(day.getUser().getId(), "worday's user must have persistent identity");
		return wdRepo.saveAndFlush(day);
	}
}
