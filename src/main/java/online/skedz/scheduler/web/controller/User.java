package online.skedz.scheduler.web.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import online.skedz.scheduler.core.business.BusinessService;
import online.skedz.scheduler.core.user.UserService;


@Controller
@RequestMapping("/user")
public class User {
	
	@Autowired
	UserService userService;
	
	@Autowired
	BusinessService businessService;
	
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
		return "user/main";	
	}



	private online.skedz.scheduler.core.user.User currentUser(Principal p) {
		return userService.byUserName(p.getName());
	}
}

