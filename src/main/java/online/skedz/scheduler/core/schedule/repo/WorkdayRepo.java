package online.skedz.scheduler.core.schedule.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import online.skedz.scheduler.core.schedule.Workday;
import online.skedz.scheduler.core.user.User;

public interface WorkdayRepo extends JpaRepository<Workday, UUID>{
	List<Workday> findAllByUser(User user);
}
