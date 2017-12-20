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
@EqualsAndHashCode(of={"beginning"})

@Entity
public class Appointment {
	
	@Id
	private UUID id;
	
	@ManyToOne
	private Workday workday;
	
	private LocalDateTime beginning;
	
	private LocalDateTime created;
	private LocalDateTime confirmed = LocalDateTime.MIN;
	
	private String clientName;
	private String clientEmail;


	public void verify() {
		this.confirmed = LocalDateTime.now();
	}
	
	LocalDateTime getEnd(){
		return beginning.plusMinutes(service.getDuration());
	}
	
	@OneToOne
	private ServiceType service;

	private UUID verificationCode;
	
	public boolean overlapsWith(Appointment other){
		if(this.equals(other)) return true;
		if(this.beginning.isBefore(other.getBeginning()) && this.getEnd().isAfter(other.getBeginning())) return true;
		if(this.getEnd().isAfter(other.getBeginning()) && this.getEnd().isBefore(other.getEnd())) return true;
		return false;
	}
	
	@PrePersist
	void init(){
		this.verificationCode = UUID.randomUUID();
		this.id = UUID.randomUUID();
		created = LocalDateTime.now();
	}

}
