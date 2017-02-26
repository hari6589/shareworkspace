package app.bsro.model.appointment;

public class AppointmentChoice {
	private String month;
	private String date;
	private String time;
	private String dropWait;
	
	public AppointmentChoice(){}
	
	public AppointmentChoice(String month, String date, String time, String dropWait) {
		super();
		this.month = month;
		this.date = date;
		this.time = time;
		this.dropWait = dropWait;
	}

	public void setDate(String date) {
		this.date = date;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getDate() {
		return date;
	}
	public String getMonth() {
		return month;
	}
	public String getTime() {
		return time;
	}
	public void setDropWait(String dropWait) {
		this.dropWait = dropWait;
	}
	public String getDropWait() {
		return dropWait;
	}
	
	@Override
	public String toString() {
		return "AppointmentChoice [month=" + month + ", date=" + date
				+ ", time=" + time + ", dropWait=" + dropWait + "]";
	}
}
