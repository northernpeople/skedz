package online.skedz.scheduler.core.business;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import online.skedz.scheduler.core.user.User;

@Setter
@Getter
@Accessors(chain=true)
@EqualsAndHashCode(of={"name", "duration"})
@ToString(exclude={"providers"})

@Entity
public class ServiceType {

	@Id
	private UUID id;
	
	@NotNull
	@Size(min=5, max=255, message="Service name must have between 5 and 255 characters")
	private String name;
	
	@Min(1)
	private long duration;

	@ManyToMany(targetEntity=User.class, mappedBy="servicesProvided")
	private Set<User> providers = new HashSet<>();
	
	@PrePersist
	void init() {
		this.id = UUID.randomUUID(); 
	}
}
