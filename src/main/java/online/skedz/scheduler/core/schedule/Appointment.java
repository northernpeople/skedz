package online.skedz.scheduler.core.schedule;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import online.skedz.scheduler.core.business.ServiceType;

@Setter
@Getter
@Accessors(chain=true)
@ToString
@EqualsAndHashCode(of={"beginning", "end"})

@Entity
public class Appointment {
	
	@Id
	private UUID id;
	
	@ManyToOne
	private Workday workday;
	
	@OneToOne
	private ServiceType service;
	
	@PrePersist
	void init(){
		this.id = UUID.randomUUID();
	}

}
