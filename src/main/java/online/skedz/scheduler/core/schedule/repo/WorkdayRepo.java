package online.skedz.scheduler.core.schedule.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import online.skedz.scheduler.core.schedule.Workday;

public interface WorkdayRepo extends JpaRepository<Workday, UUID>{

}
