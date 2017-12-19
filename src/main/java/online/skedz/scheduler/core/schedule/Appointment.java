package online.skedz.scheduler.core.schedule;

import java.time.LocalDateTime;
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
	
	private LocalDateTime beginning;
	
	LocalDateTime getEnd(){
		return beginning.plusMinutes(service.getDuration());
	}
	
	@OneToOne
	private ServiceType service;
	
	public boolean overlapsWith(Appointment other){
		if(this.equals(other)) return true;
		if(this.beginning.isBefore(other.getBeginning()) && this.getEnd().isAfter(other.getBeginning())) return true;
		if(this.getEnd().isAfter(other.getBeginning()) && this.getEnd().isBefore(other.getEnd())) return true;
		return false;
	}
	
	@PrePersist
	void init(){
		this.id = UUID.randomUUID();
	}

}
