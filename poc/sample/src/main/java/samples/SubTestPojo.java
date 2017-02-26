package samples;

public class SubTestPojo {
	
	private String choice;
	private String datetime;
	private String dropWaitOption;
	
	public String getChoice() {
		return choice;
	}
	public void setChoice(String choice) {
		this.choice = choice;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public String getDropWaitOption() {
		return dropWaitOption;
	}
	public void setDropWaitOption(String dropWaitOption) {
		this.dropWaitOption = dropWaitOption;
	}
	
	@Override
	public String toString() {
		return "SubTestPojo [choice=" + choice + ", datetime=" + datetime
				+ ", dropWaitOption=" + dropWaitOption + "]";
	}
	
}
