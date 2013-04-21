/*
Copyright (c) 2011, 2012 Parjanya Mudunuri

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

http://opensource.org/licenses/mit-license.php
 */

package uk.co.firstzero.diff;

public class Difference {
	private Integer lineNumber;
    private String expected;
	private String reached;
    private String key;
	private String mismatchType;
    private String column;
	
	public Difference(Integer lineNumber, String column, String mismatchType, String expected, String reached, String key) {
		this.setLineNumber(lineNumber);
		this.setMismatchType(mismatchType);
		this.setExpected(expected);
		this.setReached(reached);
        this.setKey(key);
        this.setColumn(column);
	}

	public void setExpected(String expected) {
		this.expected = expected;
	}

	public String getExpected() {
		return expected;
	}

	public void setReached(String reached) {
		this.reached = reached;
	}

	public String getReached() {
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

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
	
	public String toString() {
		return "Line - " + lineNumber + 
		", Mismatch Type - " + mismatchType +
		", Expected - " + (String)expected + 
		", Reached - " + (String)reached;
	}
}