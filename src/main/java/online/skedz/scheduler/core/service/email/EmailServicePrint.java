package online.skedz.scheduler.core.service.email;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class EmailServicePrint implements EmailService {

	public void send(String to, String subject, String text) {
		System.out.printf("To: %s \nSubject: %s\nContent: %s\n", to, subject, text);
    }
}
