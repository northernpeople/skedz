package online.skedz.scheduler.web.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter 
@Getter
@NoArgsConstructor
public class ChangePassword {
	
	
	@NotNull
	@Size(min=7, max=255)
	private String old;
	
	@NotNull
	@Size(min=7, max=255)
	private String new1;
	
	@NotNull
	@Size(min=7, max=255)
	private String new2;
	
	
	public ChangePassword(String old, String new1, String new2) {
		this.old = old;
		this.new1 = new1;
		this.new2 = new2;
	}
	
	
}
