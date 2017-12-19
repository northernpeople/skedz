package online.skedz.scheduler.web.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import online.skedz.scheduler.core.business.BusinessService;
import online.skedz.scheduler.core.schedule.WorkdayService;
import online.skedz.scheduler.core.service.email.EmailService;
import online.skedz.scheduler.core.user.UserService;

@Controller
@RequestMapping("/client")
public class Client {

	
	@Autowired
	EmailService emailService;
	
	@Autowired
	BusinessService businessService;
	
	@Autowired
	WorkdayService wdService;
	
	@Autowired
	UserService uService;
	
	@RequestMapping(value = "/business/{id}", method = RequestMethod.GET)
	public String findBusinessServices(@PathVariable("id") UUID id, Model model){
		model.addAttribute("business", businessService.businessById(id));
		return "client/business";
	}
	
	
	@RequestMapping(value = "/book/{id}", method = RequestMethod.GET)
	public String bookBusinessService(@PathVariable("id") UUID serviceTypeId, Model model){
		model.addAttribute("providers", uService.findByServicesProvidedId(serviceTypeId));
		model.addAttribute("service", businessService.byId(serviceTypeId));
		return "client/providers";
	}
	
	@RequestMapping(value = "/book_provider/{pid}/{sid}", method = RequestMethod.GET)
	public String bookBusinessServiceWithProvider(
			@PathVariable("sid") UUID serviceTypeId,
			@PathVariable("pid") UUID providerId, Model model){
		model.addAttribute("service", businessService.byId(serviceTypeId));
		model.addAttribute("workdays", wdService.getUpcomingWorkdaysOf(uService.byId(providerId)));

		return "client/workdays";
	}
	
	@RequestMapping(value = "/book_slots/{did}/{sid}", method = RequestMethod.GET)
	public String bookBusinessServiceWithProviderOn(	@PathVariable("sid") UUID serviceTypeId,
														@PathVariable("did") UUID workDayId, 
														Model model){

		model.addAttribute("slots", wdService.possibleSlotsOn(wdService.byId(workDayId),  businessService.byId(serviceTypeId)));
		return "client/slots";
	}
}
