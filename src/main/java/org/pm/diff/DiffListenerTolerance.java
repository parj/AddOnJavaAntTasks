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

package org.pm.diff;

import java.util.HashMap;

public class DiffListenerTolerance implements DiffListener {
	HashMap<String, Float> tolerances;
	
	public DiffListenerTolerance(HashMap<String, Float> tolerances) {
		this.tolerances = tolerances;
	}
	
	public boolean ignore(String column, String expected, String reached) {
		Float tolerance = tolerances.containsKey(column) ? tolerances.get(column) : tolerances.get("*");
		
		if (tolerance != null) {
			String strReplacedE = expected.replaceAll(",", "");
			String strReplacedR = reached.replaceAll(",", "");
			
			try {
				float floatE = new Float(strReplacedE).floatValue();
				float floatR = new Float(strReplacedR).floatValue();
				float diff = Math.abs(floatE - floatR);
				
				return diff < tolerance.floatValue() ? true : false;
			} catch(Exception e) {
				return false;
			}
		}
		else
			return false;
	}

}
