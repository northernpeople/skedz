package online.skedz.scheduler.core.schedule;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import online.skedz.scheduler.core.user.User;

@Setter
@Getter
@Accessors(chain=true)
@ToString(exclude={"user", "appointments"})
@EqualsAndHashCode(of={"beginning", "end"})

@Entity
public class Workday {
	
	@Id
	private UUID id;
	
	private LocalDateTime beginning = LocalDateTime.now();
	private LocalDateTime end  = LocalDateTime.now().plusHours(8);
	
	@ManyToOne
	private User user;
	
	@OneToMany(mappedBy="workday")
	private Set<Appointment> appointments = new HashSet<>();
	
	@PrePersist
	void init(){
		this.id = UUID.randomUUID();
	}
	
}
