package online.skedz.scheduler.web.command;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;


@Setter 
@Getter
@Accessors(chain=true)
@ToString
public class ChooseSlot {
	
	
	public ChooseSlot() { this.id = UUID.randomUUID(); }
	private UUID id;
	private UUID serviceTypeId;
	private UUID workdayId;
	private LocalDateTime beginning;
	
}
