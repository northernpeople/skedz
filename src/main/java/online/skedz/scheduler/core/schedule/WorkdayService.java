package online.skedz.scheduler.core.schedule;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import online.skedz.scheduler.core.schedule.repo.WorkdayRepo;
import online.skedz.scheduler.core.user.User;

@Service
public class WorkdayService {
	
	@Autowired
	WorkdayRepo wdRepo;
	
	public Workday createWorkday(Workday day){
		Assert.notNull(day, "workday cannot be null");
		Assert.notNull(day.getUser(), "workday must have user");
		Assert.notNull(day.getUser().getId(), "workday's user must have persistent identity");
		Assert.isTrue(! anyOverlaps(day), "workday cannot overlap with any other days");
		return wdRepo.saveAndFlush(day);
	}
	
	public boolean anyOverlaps(Workday day){
		for(Workday wd : getAllWorkdaysOf(day.getUser())){
			if(wd.overlapsWith(day)){
				return true;
			}
		}
		return false;
	}
	
	public Workday aNewDayLikeLastOne(User u){
		Workday result = getAllWorkdaysOf(u).stream()
				.sorted( (a, b) -> b.getEnd().compareTo(a.getEnd()))
				.findFirst()
				.orElseGet(() -> new Workday());
				
		return new Workday()
				.setBeginning(result.getBeginning().plusHours(24))
				.setEnd(result.getEnd().plusHours(24));
	}
	
	public void deleteWorkday(Workday day){
		Assert.notNull(day, "workday cannot be null");
		Assert.notNull(day.getId(), "workday must have id");
		wdRepo.delete(day);
	}
	
	
	public List<Workday> getUpcomingWorkdaysOf(User u){
		return getAllWorkdaysOf(u).stream()
				.filter( d -> d.getBeginning().isAfter(LocalDateTime.now()))
				.collect(Collectors.toList());
	}
	public List<Workday> getAllWorkdaysOf(User u){
		return wdRepo.findAllByUser(u).stream()
				.sorted( (a, b) -> a.getEnd().compareTo(b.getEnd()))
				.distinct()
				.collect(Collectors.toList());
	}
}
