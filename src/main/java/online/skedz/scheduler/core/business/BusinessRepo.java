package online.skedz.scheduler.core.business;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BusinessRepo extends JpaRepository<Business, UUID>{

	@Query("SELECT b FROM Business b JOIN FETCH b.team WHERE b.id = ?1")
	public Business findOneWithTeam(UUID id);
}
