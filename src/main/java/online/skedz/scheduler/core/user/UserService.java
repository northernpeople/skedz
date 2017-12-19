package online.skedz.scheduler.core.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import online.skedz.scheduler.core.business.Business;
import online.skedz.scheduler.core.business.ServiceType;




@Component
public class UserService implements UserDetailsService{

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	PasswordEncoder encoder;

	public User byUserName(String username) {
		return userRepo.findByUsername(username);
	}

	public List<User> findAll() {
		return userRepo.findAll();
	}
	
	public User addServiceTypeTo(User u, ServiceType type){
		Assert.notNull(type, "service cannot be null");
		Assert.notNull(u, "user cannot be null");
		Assert.notNull(type.getId(), "service must have persistent id");
		Assert.notNull(u.getId(), "user must have persistent id");
		u.getServicesProvided().add(type);
		type.getProviders().add(u);
		return userRepo.saveAndFlush(u);
	}
	
	public User removeServiceTypeFrom(User u, ServiceType type){
		Assert.notNull(type, "service cannot be null");
		Assert.notNull(u, "user cannot be null");
		Assert.notNull(type.getId(), "service must have persistent id");
		Assert.notNull(u.getId(), "user must have persistent id");
		u.getServicesProvided().remove(type);
		type.getProviders().remove(u);
		return userRepo.saveAndFlush(u);
	}
	
	public User addUserToBusiness(Business b, User u){
		Assert.notNull(b, "business cannot be null");
		Assert.notNull(u, "user cannot be null");
		Assert.notNull(b.getId(), "business must have persistent id");
		Assert.notNull(u.getId(), "user must have persistent id");
		b.getTeam().add(u);
		u.setBusiness(b);
		return userRepo.saveAndFlush(u);
	}
	
	/**
	 * Creates new user and hashes password.
	 * @param user with plain text password
	 * @return 
	 */
	public User create(User user, Role inRole) {
		if( ! usernameTaken(user)){
			user.setRole(inRole);
			user.setPassword(encoder.encode(user.getPassword()));
			user = saveAndFlush(user);
			return user;
		}	
		return null;
	}
	
	public User verifyEmail(String verificationCode){
		User user = userRepo.findOneByVerificationCode(verificationCode);
		user.setEmailVerified(LocalDateTime.now());
		return userRepo.saveAndFlush(user);
	}

	

	public boolean usernameTaken(User user) {
		return findAll().stream().map( u -> u.getUsername()).collect(Collectors.toSet()).contains(user.getUsername());
	}
	
	/**
	 * Updates user and rehashes password.
	 * @param user user with plain text password
	 */
	public void rehashPassword(User user){
		user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
		userRepo.saveAndFlush(user);
	}

	public User byId(UUID id) {
		return userRepo.findOne(id);
	}

	@Secured("ROLE_ADMIN")
	public void delete(UUID id) {
		userRepo.delete(id);	
	}


	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		UserDetails details = byUserName(userName);
		if (details == null) throw new UsernameNotFoundException("user not found: "+userName);
		return details;
	}

	public User saveAndFlush(User user) {
		return userRepo.saveAndFlush(user);
		
	}
	
	public User currentUser(){
		return byUserName(
				SecurityContextHolder.getContext().getAuthentication().getName()
				);
	}

	public User save(User u) {
		return userRepo.saveAndFlush(u);
		
	}

	public List<User> findByServicesProvidedId(UUID serviceTypeId) {
		return userRepo.findByServicesProvidedId(serviceTypeId);
	}

	
}
