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

import online.skedz.scheduler.core.service.email.EmailService;
import online.skedz.scheduler.core.user.Role;
import online.skedz.scheduler.core.user.User;
import online.skedz.scheduler.core.user.UserService;



@Controller
public class Register{
	
	@Autowired
	UserService userService;

	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	EmailService emailService;
	
	@GetMapping(value ="/verifyEmail/{verificationCode}")
	public String verifyEmail(	@PathVariable("verificationCode") String verificationCode, RedirectAttributes model){ 
		System.out.println(userService.verifyEmail(verificationCode));
		model.addFlashAttribute("messages", "Thank you for verifying your email, please log in");
		return "redirect:/login";
	}

	@PostMapping(value = "/register")
	public String register(@Valid @ModelAttribute("userToRegister") User user, Errors errors, RedirectAttributes model) {
		if (errors.hasErrors()) {
			model.addAttribute("userToRegister", user);
			return "register";
		}

		if (userService.usernameTaken(user)) {
			errors.rejectValue("username", "Match", "This username is taken.");
			model.addAttribute("userToRegister", user);
			return "register";
		}

		user = userService.create(user, Role.ROLE_USER); // this method checks if user already exists
		model.addFlashAttribute("messages", "We send you an email, please follow a link in that email to verify your email address, please also check spam folder");
		emailService.send(user.getUsername(), 
				"Welcome to remindeme app", 
				"Please follow this link: "
				+ "http://localhost:8080/verifyEmail/"+user.getVerificationCode()
				+ " this link will expire in 7 days.");
		return "redirect:/login";
	}
	
	@RequestMapping(value = "/register_form", method = RequestMethod.GET)
	public String registerForm(Model m){
		m.addAttribute("registration_request", new User());
		return "register_form";	
	}
	

	
	
}
