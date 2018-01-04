package online.skedz.scheduler.core.schedule;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import online.skedz.scheduler.core.business.ServiceType;
import online.skedz.scheduler.core.schedule.repo.AppointmentRepo;
import online.skedz.scheduler.core.schedule.repo.WorkdayRepo;
import online.skedz.scheduler.core.user.User;

@Service
public class WorkdayService {
	
	@Autowired
	WorkdayRepo wdRepo;
	
	@Autowired
	AppointmentRepo apRepo;
	
	public Appointment appointmentById(UUID appointmentId){
		return apRepo.getOne(appointmentId);
	}
	public Appointment appointmentByVerification(UUID verificationCode){
		return apRepo.findOneByVerificationCode(verificationCode);
	}
	
	public Appointment createAppointment(Appointment a){
		Assert.notNull(a.getBeginning(), "begging time cannot be null");
		Assert.notNull(a.getService(), "service type cannot be null");
		Assert.notNull(a.getService().getId(), "service type id cannot be null");
		Assert.notNull(a.getWorkday(), "workday cannot be null");
		Assert.notNull(a.getWorkday().getId(), "workday id cannot be null");

		return apRepo.saveAndFlush(a);
	}
	
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
	
	public Workday byId(UUID id){
		return wdRepo.findOne(id);
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
				.filter( d -> d.getEnd().isAfter(LocalDateTime.now()))
				.collect(Collectors.toList());
	}
	public List<Workday> getAllWorkdaysOf(User u){
		return wdRepo.findAllByUser(u).stream()
				.sorted( (a, b) -> a.getEnd().compareTo(b.getEnd()))
				.distinct()
				.collect(Collectors.toList());
	}
	

	/**
	 * Calculates all possible appointments that can still be booked on that day.
	 * @param day
	 * @param type
	 * @return
	 */
	public List<Appointment> possibleSlotsOn(Workday day, ServiceType type){
		List<Appointment> possible = new ArrayList<>();
		for(LocalDateTime time = day.getBeginning(); time.isBefore(day.getEnd()); time = time.plusMinutes(type.getDuration())){
			possible.add(new Appointment().setService(type).setBeginning(time).setWorkday(day));
		}
		for(Appointment existing : day.getAppointments())
			possible.removeIf(p -> existing.overlapsWith(p));
			
		return possible;
	}

	public void deleteWorkday(UUID workdayId) {
		wdRepo.delete(workdayId);
	}
	public Appointment save(Appointment requested) {
		return apRepo.saveAndFlush(requested);
		
	}
	
	public void cancelAppointment(UUID appointmentId) {
		apRepo.delete(appointmentId);
		
	}
}
