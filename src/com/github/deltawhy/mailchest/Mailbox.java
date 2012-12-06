package com.github.deltawhy.mailchest;

import java.io.Serializable;

public class Mailbox implements Serializable {
	private static final long serialVersionUID = 1L;

	private String ownerName;
	
	public Mailbox(String ownerName) {
		this.setOwnerName(ownerName);
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	
	public String toString() {
		return String.format("<Mailbox owner=%s>", ownerName);
	}
}
