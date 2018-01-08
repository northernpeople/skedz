package online.skedz.scheduler.web.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import online.skedz.scheduler.core.business.BusinessService;
import online.skedz.scheduler.core.schedule.Appointment;
import online.skedz.scheduler.core.schedule.Workday;
import online.skedz.scheduler.core.schedule.WorkdayService;
import online.skedz.scheduler.core.service.email.EmailService;
import online.skedz.scheduler.core.user.UserService;


@Controller
@RequestMapping("/user")
public class User {
	
	@Autowired
	UserService userService;
	
	@Autowired
	BusinessService businessService;
	
	@Autowired
	WorkdayService wdService;
	
	@Autowired
	EmailService emailService;
	
	
	@RequestMapping(value = "/create_workday", method = RequestMethod.POST)
	public String createWorkday(@Valid @ModelAttribute("workday") Workday day, RedirectAttributes model, Principal p){
		if(day.getBeginning().isAfter(day.getEnd())){
			model.addFlashAttribute("messages", Arrays.asList("start must be before the end"));
			return "redirect:/user/main";
		}
		day.setUser(currentUser(p));
		if(wdService.anyOverlaps(day)){
			model.addFlashAttribute("messages", Arrays.asList("Day overlaps with existing days"));
			return "redirect:/user/main";
		}
		if(day.getBeginning().isBefore(LocalDateTime.now())){
			model.addFlashAttribute("messages", Arrays.asList("Day must be in the future"));
			return "redirect:/user/main";
		}
		wdService.createWorkday(day);
		model.addFlashAttribute("messages", Arrays.asList( 
				"Clients can now book appointments between " +day.getBeginning().toLocalTime()
				+ " and "+ day.getEnd().toLocalTime() 
				+ " on " + day.getBeginning().toLocalDate(),
				"Day added"));

		return "redirect:/user/main";
	}
	@RequestMapping(value = "/delete_workday/{id}", method = RequestMethod.GET)
	public String deleteWorkday(@PathVariable("id") UUID workdayId, RedirectAttributes model, Principal p){
		if(! dayBelongsToUser(workdayId, p)){
			return "redirect:/user/main";
		}
		for(Appointment cancelled : wdService.byId(workdayId).getAppointments()){
			wdService.cancelAppointment(cancelled.getId());
			emailService.send(cancelled.getClientEmail(), 
					"Hi " + cancelled.getClientName(), 
					"Your appointment: "
					+ cancelled.getService().getName()
					+ " on "
					+ cancelled.getBeginning().toLocalDate()
					+ " / "
					+ cancelled.getBeginning().toLocalTime()
					+ " has been CANCELLED. Please rebook or contact service provider for more information.");
			model.addFlashAttribute("messages", Arrays.asList("All appointments cancelled", "Notification emails sent to customers"));
		}
		wdService.deleteWorkday(workdayId);
		return "redirect:/user/main";
	}
	
	@RequestMapping(value = "/cancell_appointment/{id}", method = RequestMethod.GET)
	public String cancelAppointment(@PathVariable("id") UUID appointmentId, RedirectAttributes model, Principal p){
		Appointment cancelled = wdService.appointmentById(appointmentId);
		wdService.cancelAppointment(appointmentId);
		emailService.send(cancelled.getClientEmail(), 
				"Hi " + cancelled.getClientName(), 
				"Your appointment: "
				+ cancelled.getService().getName()
				+ " on "
				+ cancelled.getBeginning().toLocalDate()
				+ " / "
				+ cancelled.getBeginning().toLocalTime()
				+ " has been CANCELLED. Please rebook or contact service provider for more information.");
		model.addFlashAttribute("messages", Arrays.asList("Appointment cancelled", "Notification email sent"));
		return "redirect:/user/main";
	}
	
	private boolean dayBelongsToUser(UUID workdayId, Principal p) {
		return wdService.getAllWorkdaysOf(currentUser(p)).stream().filter(d -> d.getId().equals(workdayId)).findFirst().isPresent();
	}
	
	
	@RequestMapping(value = "/add_service/{id}", method = RequestMethod.GET)
	public String addService(@PathVariable("id") UUID serviceTypeId, RedirectAttributes model, Principal p){
		userService.addServiceTypeTo( currentUser(p), businessService.byId(serviceTypeId));
		model.addFlashAttribute("messages", Arrays.asList("Clients can now book the following service with you:", businessService.byId(serviceTypeId).getName()));
		return "redirect:/user/main";
	}
	
	@RequestMapping(value = "/delete_service_type/{id}", method = RequestMethod.GET)
	public String deleteService(@PathVariable("id") UUID serviceTypeId, RedirectAttributes model, Principal p){
		userService.removeServiceTypeFrom( currentUser(p), businessService.byId(serviceTypeId));
		model.addFlashAttribute("messages", Arrays.asList("Clients can no longer book this service with you:", businessService.byId(serviceTypeId).getName()));
		return "redirect:/user/main";
	}
	
	@GetMapping(value = "/main")
	public String main(Model m, Principal p){
		m.addAttribute("current_user", currentUser(p));
		m.addAttribute("workday", wdService.aNewDayLikeLastOne(currentUser(p)));
		m.addAttribute("workdays", wdService.getUpcomingWorkdaysOf(currentUser(p)));
		m.addAttribute("appointments", 
				wdService.getUpcomingWorkdaysOf(currentUser(p))
				.stream()
				.flatMap(wd -> wd.getAppointments().stream())
				.collect(Collectors.toSet()));

		return "user/main";	
	}



	private online.skedz.scheduler.core.user.User currentUser(Principal p) {
		return userService.byUserName(p.getName());
	}
}

