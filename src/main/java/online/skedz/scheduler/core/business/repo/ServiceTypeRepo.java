package online.skedz.scheduler.core.business.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import online.skedz.scheduler.core.business.ServiceType;

public interface ServiceTypeRepo extends JpaRepository<ServiceType, UUID>{	
}
