package online.skedz.scheduler.web.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import online.skedz.scheduler.core.business.BusinessService;
import online.skedz.scheduler.core.business.ServiceType;
import online.skedz.scheduler.core.service.email.EmailService;
import online.skedz.scheduler.core.user.Role;
import online.skedz.scheduler.core.user.User;
import online.skedz.scheduler.core.user.UserService;
import online.skedz.scheduler.web.command.InviteUser;




@Controller
@RequestMapping("/admin")
public class Admin {
	
	@Autowired
	UserService userService;

	@Autowired
	EmailService emailService;
	
	@Autowired
	BusinessService bService;
	
	
	@RequestMapping(value = "/reset_pass_user/{id}", method = RequestMethod.GET)
    public String resetPassword (@PathVariable("id") UUID id, RedirectAttributes model) {
		String tempPassword = UUID.randomUUID().toString().replaceAll("-", "");
		User user = userService.byId(id);
		user.setPassword(tempPassword);
		userService.rehashPassword(user);
		emailService.send(user.getUsername(), "Your password has been reset", "Use the following temporary password: "+ tempPassword);
		model.addFlashAttribute("messages", Arrays.asList("Password reset", "Inform user to check email with new password"));
		return "redirect:/admin/main";
	}
	
	@RequestMapping(value = "/delete_user/{id}", method = RequestMethod.GET)
    public String deleteUser (@PathVariable("id") UUID id, RedirectAttributes model) {
		if(! userService.byId(id).getRole().equals(Role.ROLE_ADMIN)){
			model.addFlashAttribute("messages", Arrays.asList("Account deleted"));
			userService.delete(id);
			return "redirect:/admin/main";
		}
		model.addFlashAttribute("messages", Arrays.asList("Admin account cannot be deleted"));
		return "redirect:/admin/main";
	}
	
	@RequestMapping(value = "/createuser", method = RequestMethod.POST)
	public String createUser(@Valid @ModelAttribute("user") InviteUser invitedUser, Errors errors, RedirectAttributes model, Principal principal) {
		if(errors.hasErrors()){
			model.addFlashAttribute("messages", Arrays.asList("Doesnt look like a valid email", "Please try again"));
			return "redirect:/admin/main";
		}
		
		User candidate = new User().setUsername(invitedUser.getUsername());
		String tempPassword = UUID.randomUUID().toString().replaceAll("-", "");
		
		candidate.setPassword(tempPassword);
		
		candidate = userService.create(candidate, Role.ROLE_USER);
		User current = userService.byUserName(principal.getName());
		userService.addUserToBusiness(current.getBusiness(), candidate);
		
		emailService.send(candidate.getUsername(), 
				"You have been invited to " + current.getBusiness().getName(), 
				"Please follow this link: "
				+ "http://localhost:8080/verifyEmail/"+candidate.getVerificationCode()
				+ " and use this temporary password: "+ tempPassword);
		
		model.addFlashAttribute("messages", Arrays.asList("Invitation sent!"));
		return "redirect:/admin/main";
	}
	
	
	@RequestMapping(value = "/edit_service_type/{id}", method = RequestMethod.GET)
	public String editServiceType(@PathVariable("id") UUID serviceTypeId, Model model){
		model.addAttribute("service_type", bService.byId(serviceTypeId));
		return "admin/servicetype/edit";
	}
	
	
	@RequestMapping(value = "/create_service_type", method = RequestMethod.POST)
	public String createServiceType(@Valid ServiceType type, Errors errors, RedirectAttributes model, Principal p){
		
		User current = userService.byUserName(p.getName());
		type = bService.create(type);
		bService.addServiceType(current.getBusiness(), type);
		return "redirect:/admin/main";
	}
	
	
	@RequestMapping("/main")
	public String main(Model m, Principal principal){
		System.out.println(m.asMap());
		User current = userService.byUserName(principal.getName());
		m.addAttribute("service_type", new ServiceType());
		m.addAttribute("service_types", current.getBusiness().getServicesProvided());
		m.addAttribute("user", new InviteUser());
		m.addAttribute("users", bService.getOneWithTeam(current.getBusiness().getId()).getTeam());
		return "admin/main";
	}
	
	@RequestMapping(value = "/delete_service_type/{id}", method = RequestMethod.GET)
	public String deleteServiceType(@PathVariable("id") UUID id, Principal principal){
		User current = userService.byUserName(principal.getName());
		bService.deleteServiceType(current.getBusiness(), bService.byId(id));

		return "redirect:/admin/main";
	}

}
