package org.pm.diff.exception;

public class KeyColumnsMissingException extends Exception {
	String message;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7641546142300243844L;
	
	public KeyColumnsMissingException(String message) {
		this.message = message;
	}
	
	public String toString() {
		return this.message;
	}
}
