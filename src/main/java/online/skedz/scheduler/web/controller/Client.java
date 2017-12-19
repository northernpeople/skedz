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

@Controller
@RequestMapping("/client")
public class Client {

	
	@Autowired
	EmailService emailService;
	
	@Autowired
	BusinessService businessService;
	
	@Autowired
	WorkdayService wdService;
	
	@RequestMapping(value = "/business/{id}", method = RequestMethod.GET)
	public String findBusinessServices(@PathVariable("id") UUID id, Model model){
		model.addAttribute("business", businessService.businessById(id));
		return "client/business";
	}
}
