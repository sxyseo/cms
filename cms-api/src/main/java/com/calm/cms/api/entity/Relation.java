package com.calm.cms.api.entity;

public enum Relation {
	ONE2ONE("ONE2ONE", "一对一"), ONE2MANY("ONE2MANY", "一对多"),MANY2ONE("MANY2ONE", "多对一"), MANY2MANY(
			"MANY2MANY", "多对多");
	private String name;
	private String displayName;

	Relation(String name, String displayName) {
		this.setName(name);
		this.displayName = displayName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
}
