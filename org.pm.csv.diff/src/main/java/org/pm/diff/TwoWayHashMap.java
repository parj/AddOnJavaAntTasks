package org.pm.diff;

import java.util.HashMap;

import org.apache.log4j.Logger;

public class TwoWayHashMap {
	private HashMap<String, Integer> stringIntegerMap;
	private HashMap<Integer, String> integerStringMap;
	
	private static Logger logger = Logger.getLogger(TwoWayHashMap.class);
	
	public TwoWayHashMap() {
		stringIntegerMap = new HashMap<String, Integer>();
		integerStringMap = new HashMap<Integer, String>();
	}
	
	public void put(String value, Integer lookup) {
		logger.trace("Putting " + value + " " + lookup);
		stringIntegerMap.put(value, lookup);
		integerStringMap.put(lookup, value);
	}
	
	public void put(Integer lookup, String value) {
		put(value, lookup);
	}
	
	public void remove(String value) {
		logger.trace("Removing " + value);
		Integer lookup = stringIntegerMap.get(value);
		stringIntegerMap.remove(value);
		integerStringMap.remove(lookup);
	}
	
	public void remove(Integer lookup) {
		logger.trace("Removing " + lookup);
		String value = integerStringMap.get(lookup);
		stringIntegerMap.remove(value);
		integerStringMap.remove(lookup);
	}
	
	public Integer get(String value) {
		return stringIntegerMap.get(value);
	}
	
	public String get(Integer lookup) {
		return integerStringMap.get(lookup);
	}
	
	public boolean containsKey(String value) {
		return stringIntegerMap.containsKey(value);
	}
	
	public boolean containsKey(Integer lookup) {
		return integerStringMap.containsKey(lookup);
	}
}
