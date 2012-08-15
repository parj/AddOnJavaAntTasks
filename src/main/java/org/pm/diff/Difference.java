package org.pm.diff;

public class Difference {
	private Integer lineNumber;
	private Object expected;
	private Object reached;
	private String mismatchType;
	
	public Difference(Integer lineNumber, String mismatchType, Object expected, Object reached) {
		this.setLineNumber(lineNumber);
		this.setMismatchType(mismatchType);
		this.setExpected(expected);
		this.setReached(reached);
	}

	public void setExpected(Object expected) {
		this.expected = expected;
	}

	public Object getExpected() {
		return expected;
	}

	public void setReached(Object reached) {
		this.reached = reached;
	}

	public Object getReached() {
		return reached;
	}

	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}

	public Integer getLineNumber() {
		return lineNumber;
	}

	public void setMismatchType(String mismatchType) {
		this.mismatchType = mismatchType;
	}

	public String getMismatchType() {
		return mismatchType;
	}
	
	public String toString() {
		return "Line - " + lineNumber + 
		", Mismatch Type - " + mismatchType +
		", Expected - " + (String)expected + 
		", Reached - " + (String)reached;
	}
}
