package online.skedz.scheduler.web.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PasswordChange {
	
	
	@NotNull
	@Size(min=7, max=255)
	private String old;
	
	@NotNull
	@Size(min=7, max=255)
	private String new1;
	
	@NotNull
	@Size(min=7, max=255)
	private String new2;
	
	
	public PasswordChange(String old, String new1, String new2) {
		this.old = old;
		this.new1 = new1;
		this.new2 = new2;
	}
	
	public PasswordChange(){}

	
	public String getOld() {
		return old;
	}
	public String getNew1() {
		return new1;
	}
	public String getNew2() {
		return new2;
	}
	public void setOld(String old) {
		this.old = old;
	}
	public void setNew1(String new1) {
		this.new1 = new1;
	}
	public void setNew2(String new2) {
		this.new2 = new2;
	}
	
	

}
