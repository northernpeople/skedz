package online.skedz.scheduler.core.service.email;

public interface EmailService {
	public void send(String to, String subject, String text) ;
	
	public String getServerUrl();
}
