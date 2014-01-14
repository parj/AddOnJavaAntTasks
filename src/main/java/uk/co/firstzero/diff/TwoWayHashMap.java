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

import org.apache.log4j.Logger;

import java.util.HashMap;

/**
 * A hashmap that can lookup key & value and lookup key based on value
 */
public class TwoWayHashMap {
	private HashMap<String, Integer> stringIntegerMap;
	private HashMap<Integer, String> integerStringMap;
	
	private static Logger logger = Logger.getLogger(TwoWayHashMap.class);

    /**
     * Default constructor
     */
	public TwoWayHashMap() {
		stringIntegerMap = new HashMap<String, Integer>();
		integerStringMap = new HashMap<Integer, String>();
	}

    /**
     * The value to be put
     * @param value The value
     * @param lookup The key
     */
	public void put(String value, Integer lookup) {
		logger.trace("Putting " + value + " " + lookup);
		stringIntegerMap.put(value, lookup);
		integerStringMap.put(lookup, value);
	}

    /**
     * The key to be inserted
     * @param lookup The key
     * @param value The value
     */
	public void put(Integer lookup, String value) {
		put(value, lookup);
	}

    /**
     * The value to be removed and the corresponding key
     * @param value The value to be located
     */
	public void remove(String value) {
		logger.trace("Removing " + value);
		Integer lookup = stringIntegerMap.get(value);
		stringIntegerMap.remove(value);
		integerStringMap.remove(lookup);
	}

    /**
     * Removes the key and corresponding value
     * @param lookup The key to be located
     */
	public void remove(Integer lookup) {
		logger.trace("Removing " + lookup);
		String value = integerStringMap.get(lookup);
		stringIntegerMap.remove(value);
		integerStringMap.remove(lookup);
	}

    /**
     * Looks up key based on value
     * @param value The value
     * @return Returns the key
     */
	public Integer get(String value) {
		return stringIntegerMap.get(value);
	}

    /**
     * Looks up value based on key
     * @param lookup The key
     * @return Returns the value
     */
	public String get(Integer lookup) {
		return integerStringMap.get(lookup);
	}

    /**
     * Returns true/false if the value could be located
     * @param value The value to be looked up
     * @return Returns true/false if the value could be located
     */
	public boolean containsKey(String value) {
		return stringIntegerMap.containsKey(value);
	}

    /**
     * Returns true/false if the key could be located
     * @param lookup The key to be looked up
     * @return Returns true/false if the key could be located
     */
	public boolean containsKey(Integer lookup) {
		return integerStringMap.containsKey(lookup);
	}
}
