package online.skedz.scheduler.web.command;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Setter 
@Getter
@Accessors(chain=true)
@ToString
public class ClientConfirmEmail {
	
	private UUID appointmentId;
	private String name;
	private String email;
}
