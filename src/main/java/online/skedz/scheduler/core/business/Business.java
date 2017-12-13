package online.skedz.scheduler.core.business;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;

import lombok.*;
import lombok.experimental.Accessors;
import online.skedz.scheduler.core.user.User;

@Setter
@Getter
@Accessors(chain=true)


@Entity
public class Business {
	
	@Id
	private UUID id;
	
	private String name;

	@OneToMany(mappedBy="business")
	private Set<User> team = new HashSet<>();
	
	@OneToMany
	private Set<ServiceType> servicesProvided = new HashSet<>();
	
	@PrePersist
	void init() {
		this.id = UUID.randomUUID(); 
	}

}
