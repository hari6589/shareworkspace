package samples;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeManipulation {

	public static void main(String[] args) {
		
		DateTimeManipulation dt = new DateTimeManipulation();
		
		dt.extractDateAndTime();

	}
	
	public void extractDateAndTime() {
		Date today = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm");
		String dateStr = dateFormat.format(today);
	
		Date choiceDateTime = null;
		try {
			choiceDateTime = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			System.out.println("Date format parsing exception!");
		}
		
		System.out.println("Choice Date Time: " + choiceDateTime);
		
		String[] dateSplits = dateStr.split("\\s+");
		String selectedDate = dateSplits[0];
		String[] hourMinSplits = dateSplits[1].split(":");
		Integer selectedTime = new Integer(hourMinSplits[0]) * 60 + new Integer(hourMinSplits[1]);
		
		System.out.println("Selected Date : " + selectedDate);
		System.out.println("Selected Time : " + selectedTime);
	}

}
