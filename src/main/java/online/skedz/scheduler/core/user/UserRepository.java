package online.skedz.scheduler.core.user;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



public interface UserRepository extends JpaRepository<User, UUID> {

	public User findByUsername(String userName);
	public User findOneByVerificationCode(String verificationCode);
	
//	@Query("SELECT u FROM User u JOIN FETCH u.servicesProvided WHERE u.servicesProvided.id = ?1")
	public List<User> findByServicesProvidedId(UUID id);
}