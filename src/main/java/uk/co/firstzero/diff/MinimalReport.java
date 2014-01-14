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

import java.io.IOException;

/**
 * A minimal report which is printed to the std out
 */
public class MinimalReport implements Report {
	private int numberOfDifferences = 0;

    /**
     * Empty constructor
     * @throws IOException
     */
	public MinimalReport() throws IOException {
	}

    /**
     * Writes differences as those are found
     * @param difference The difference found
     */
	public void write(Difference difference) {
		++numberOfDifferences;
		System.out.println(difference.toString());
	}

    /**
     * Does nothing
     * @throws IOException
     */
	public void close() throws IOException {
        //Do nothing
	}

    /**
     * Does nothing
     * @throws IOException
     */
	@Override
	public void open(String fileName) throws IOException {
		//Do nothing
	}

    /**
     * Gets the number of differences
     * @return Gets the number of differences
     */
	public int getNumberOfDifferences() {
		return numberOfDifferences;
	}

}
