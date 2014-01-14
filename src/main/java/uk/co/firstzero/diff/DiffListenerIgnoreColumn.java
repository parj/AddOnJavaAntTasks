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

import java.util.List;

/**
 * The columns to be igored during diff
 */
public class DiffListenerIgnoreColumn implements DiffListener {
	private List<String> columns;

    /**
     * Default constructor
     * @param columns The list of columns
     */
	public DiffListenerIgnoreColumn(List<String> columns) {
		this.columns = columns;
	}

    /**
     * Adds the column to be ignored
     * @param column Adds the column name specified
     */
	public void add(String column) {
		this.add(column);
	}

    /**
     * Returns true if the column name to be ignored is found
     * @param column The column name to be checked
     * @param expected The expected value
     * @param reached The reached value
     * @return True/False based on whether the column is contained in the list
     */
	public boolean ignore(String column, String expected, String reached) {
		return columns.contains(column);
	}
}
