package online.skedz.scheduler.core.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;



public interface UserRepository extends JpaRepository<User, UUID> {

	public User findByUsername(String userName);
	public User findOneByVerificationCode(String verificationCode);
	
}