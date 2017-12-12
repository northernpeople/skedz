package online.skedz.scheduler.core.business;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;

import lombok.*;
import online.skedz.scheduler.core.user.User;

@Setter
@Getter
@Entity
public class Business {
	
	@Id
	private UUID id;
	
	private String name;

	@OneToMany
	private Set<User> team = new HashSet<>();
	
	@OneToMany
	private Set<ServiceType> servicesProvided = new HashSet<>();
	
	@PrePersist
	void init() {
		this.id = UUID.randomUUID(); 
	}

}
