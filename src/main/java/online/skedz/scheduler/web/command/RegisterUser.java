package online.skedz.scheduler.web.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter 
@Getter
@NoArgsConstructor
public class RegisterUser {
	
	@NotNull
	@Size(min=3, max=255)
	private String company;
	
	@NotNull
	@Size(min=5, max=255)
	@Pattern(regexp = "(\\d|\\w|\\.)+\\@(\\d|\\w|\\.)+\\.(\\d|\\w)+",  message = "example: john.berg@gmail.com")
	private String username;
	
	@NotNull
	@Size(min=7, max=255)
	private String password;	
}
