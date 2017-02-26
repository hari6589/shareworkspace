package com.bfrc.dataaccess.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ValueText")
public class ValueTextBean {

	private String value;
	private String text;

	public ValueTextBean() {
	}

	public ValueTextBean(String value, String text) {
		setValue(value);
		setText(text);
	}

	public ValueTextBean(String both) {
		setValue(both);
		setText(both);
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public String getValue() {
		return value;
	}

	public String toString() {
		return "ValueTextBean [value=" + value + ", text=" + text + "]";
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ValueTextBean other = (ValueTextBean) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
