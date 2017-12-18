package online.skedz.scheduler.core.formatters_converters;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component
public class MyDateTimeFormatter implements Formatter<LocalDateTime> {



	@Override
	public String print(LocalDateTime date, Locale arg1) {
		System.out.println("printing date to string");
		return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).toString();
	}

	@Override
	public LocalDateTime parse(String date, Locale arg1) throws ParseException {
		System.out.println("converting string date to localdate: date" +date);
		return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
	}

}