package online.skedz.scheduler.web.controller;

import java.security.Principal;
import java.util.Arrays;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import online.skedz.scheduler.core.user.Role;
import online.skedz.scheduler.core.user.User;
import online.skedz.scheduler.core.user.UserService;
import online.skedz.scheduler.web.command.ChangePassword;



@Controller
public class Login{
	
	@Autowired
	UserService userService;

	@Autowired
	PasswordEncoder encoder;
	
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String routeHome(Principal principal) {
		User current = userService.byUserName(principal.getName());
		switch(current.getRole()){
			case ROLE_ADMIN:
				return "redirect:/user/main";
			case ROLE_USER:
				return "redirect:/user/main";
			default:
				return "redirect:/login";	
		}	
	}

	@GetMapping(path = {"/login", "/"})
	public String userForm(Model model) {
		model.addAttribute("user", new User());
		return "user_form";
	}
	
	@RequestMapping(path="/change_password_form", method = RequestMethod.GET)
	public String changePasswordForm(Model model){
		model.addAttribute("pass_request", new ChangePassword());
		return "change_password_form";
	}
	
	@RequestMapping(path="/change_password", method=RequestMethod.POST)
	public String changePassword(@Valid @ModelAttribute("pass_request") ChangePassword changeRequest, Errors errors, Model model){
		if(errors.hasErrors()){
			model.addAttribute("pass_request", changeRequest);
			return "change_password_form";		
		}
		if(! changeRequest.getNew1().equals(changeRequest.getNew2())){
			errors.rejectValue("new2", "Match", "new passwords must match");
			model.addAttribute("pass_request", changeRequest);
			return "change_password_form";		
		}
		if( changeRequest.getOld().equals(changeRequest.getNew2())){
			errors.rejectValue("new1", "Match", "new passwords must not match the old one");
			model.addAttribute("pass_request", changeRequest);
			return "change_password_form";		
		}
		
		User currentUser = userService.currentUser();
		if(currentUser == null || ! encoder.matches(changeRequest.getOld(), currentUser.getPassword())){
			model.addAttribute("messages", Arrays.asList("Please sing in"));
			return "redirect:/";
		}
		
		currentUser.setPassword(changeRequest.getNew1());
		userService.rehashPassword(currentUser);
		model.addAttribute("messages", Arrays.asList("Password has been changed"));
		return "redirect:/logout";
	}

	
}
