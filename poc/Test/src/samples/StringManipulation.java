package samples;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StringManipulation {

	public static void main(String[] args) {
		StringManipulation sm = new StringManipulation();
		
		//sm.splitString();
		
		//sm.minutesToHours(950);
		
		//String dateStr = "20170209";
		//System.out.println(dateStr + " : " + sm.getStartTime(dateStr));
		
		sm.getDateTime();
	}
	
	public void splitString() {
		String str = "123,456,789";
		String primary = "";
		String secondary = "";
		int firstDelimIndex = str.indexOf(",");
		if(firstDelimIndex != -1) {
			primary = str.substring(0, firstDelimIndex);
			secondary = str.substring(firstDelimIndex+1);
		}
		System.out.println("Input : " + str);
		System.out.println("Primary : " + primary);
		System.out.println("Secondary : " + secondary);
	}

	public void minutesToHours(int startTime) {
		//int startTime = dataArray.getJSONObject(i).getInt("start_time");
		int hour = Math.abs(new Integer(startTime/60));
		int mins = startTime%60;
		String paddedMins = ("00"+mins).substring(("00"+mins).length()-2);
		String am_pm = "am";
		if(hour > 11){
			am_pm = "pm";
			if(hour >= 13){
				hour = hour - 12;
			}
		}
		System.out.println(hour +":"+ paddedMins + am_pm);
	}
	
	public Integer getStartTime(String selectedDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Integer startTimeInMins = 0;
		try {
			Date apptDate = dateFormat.parse(selectedDate); //Start from midnight, get slots for entire day if selected day is future
			
			Calendar today = Calendar.getInstance();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(apptDate);
			System.out.println(today);
			if(calendar.before(today)){
				SimpleDateFormat hmFormat = new SimpleDateFormat("HH:mm");
				String currentHourMin = hmFormat.format(today.getTime());
				String[] hourMinSplits = currentHourMin.split(":");
				startTimeInMins = new Integer(hourMinSplits[0]) * 60 + new Integer(hourMinSplits[1]);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return startTimeInMins;
	}
	
	public void getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm");
		String dateStr = dateFormat.format(new Date());

		Date choiceDateTime = null;
		try {
			choiceDateTime = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			System.out.println("ParseException : " + e);
		}

		String[] dateSplits = dateStr.split("\\s+");
		String selectedDate = dateSplits[0];
		String[] hourMinSplits = dateSplits[1].split(":");
		Integer selectedTime = new Integer(hourMinSplits[0]) * 60 + new Integer(hourMinSplits[1]);
		
		System.out.println("Selected Date : " + selectedDate);
		System.out.println("Selected Time : " + selectedTime);
	}
}
