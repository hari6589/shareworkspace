package com.bfrc.dataaccess.model.attribute;

/**
 * Class representing the Attribute coming from the database.<br/>
 * Note that this class uses the name attribute in the equals function so a Set or List of
 * these items will be unique based upon the name, only.
 * @author Brad Balmer
 *
 */
public class BfrcAppAttribute {
	
	private long id;
	private String type;
	private String group;
	private String name;
	private String value;
	private String status;
	private int sort;
	
	public BfrcAppAttribute(){}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BfrcAppAttribute other = (BfrcAppAttribute) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BfrcAppAttribute [id=" + id + ", type=" + type + ", group="
				+ group + ", name=" + name + ", value=" + value + ", status="
				+ status + ", sort=" + sort + "]";
	}

}
