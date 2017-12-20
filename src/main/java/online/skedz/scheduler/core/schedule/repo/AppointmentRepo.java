package online.skedz.scheduler.core.schedule.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import online.skedz.scheduler.core.schedule.Appointment;

public interface AppointmentRepo extends JpaRepository<Appointment, UUID>{

	Appointment findOneByVerificationCode(UUID verificationCode);
}
