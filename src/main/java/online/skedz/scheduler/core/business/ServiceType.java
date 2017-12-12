package online.skedz.scheduler.core.business;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import lombok.Getter;
import lombok.Setter;
import online.skedz.scheduler.core.user.User;

@Setter
@Getter
@Entity
public class ServiceType {

	@Id
	private UUID id;
	
	private String name;

	@ManyToMany(targetEntity=User.class, mappedBy="servicesProvided")
	private Set<User> providers = new HashSet<>();
	
	@PrePersist
	void init() {
		this.id = UUID.randomUUID(); 
	}
}
