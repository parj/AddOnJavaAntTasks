package org.pm.diff.exception;

public class HeaderColumnsDoNotMatchException extends Exception {
	String message;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1681159313681552754L;

	public HeaderColumnsDoNotMatchException(String message) {
		this.message = message;
	}
	
	public String toString() {
		return this.message;
	}
}
