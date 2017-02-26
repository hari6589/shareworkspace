package app.bsro.model.maintenance;

import java.io.Serializable;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class ComponentType implements Serializable {
	private static final long serialVersionUID = -5677871421395841728L;
	private String name;
	private String note;
	
	public ComponentType(){}
	public ComponentType(String name, String note) {
		setName(name);
		setNote(note);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getName() {
		return name;
	}
	public String getNote() {
		return note;
	}
}
