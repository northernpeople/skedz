package online.skedz.scheduler.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import online.skedz.scheduler.core.business.Business;
import online.skedz.scheduler.core.business.BusinessService;
import online.skedz.scheduler.core.service.email.EmailService;
import online.skedz.scheduler.core.user.Role;
import online.skedz.scheduler.core.user.User;
import online.skedz.scheduler.core.user.UserService;
import online.skedz.scheduler.web.command.RegisterUser;



@Controller
public class Register{
	
	@Autowired
	UserService userService;

	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	BusinessService businessService;
	
	@GetMapping(value ="/verifyEmail/{verificationCode}")
	public String verifyEmail(	@PathVariable("verificationCode") String verificationCode, RedirectAttributes model){ 
		System.out.println(userService.verifyEmail(verificationCode));
		model.addFlashAttribute("messages", "Thank you for verifying your email, please log in");
		return "redirect:/login";
	}

	@PostMapping(value = "/register")
	public String register(@Valid @ModelAttribute("registration_request") RegisterUser registerRequest, Errors errors, RedirectAttributes model) {
		User user = new User()
				.setUsername(registerRequest.getUsername())
				.setPassword(registerRequest.getPassword());
		
		if (errors.hasErrors()) {
			model.addAttribute("registration_request", registerRequest);
			return "register/form";
		}
		
		if (userService.usernameTaken(user)) {
			errors.rejectValue("username", "Match", "This username is taken.");
			model.addAttribute("registration_request", registerRequest);
			return "register/form";
		}

		user = userService.create(user, Role.ROLE_ADMIN); // this method checks if user already exists
		Business newB = businessService.create(new Business().setName(registerRequest.getCompany()));
		user = userService.addUserToBusiness(newB, user);
		
		model.addFlashAttribute("messages", "We send you an email, please follow a link in that email to verify your email address, please also check spam folder");
		emailService.send(user.getUsername(), 
				"Welcome to remindeme app", 
				"Please follow this link: "
				+ "http://localhost:8080/verifyEmail/"+user.getVerificationCode()
				+ " this link will expire in 7 days.");
		return "register/email_sent";
	}
	
	@RequestMapping(value = "/register_form", method = RequestMethod.GET)
	public String registerForm(Model m){
		m.addAttribute("registration_request", new RegisterUser());
		return "register/form";	
	}
	
	
}
