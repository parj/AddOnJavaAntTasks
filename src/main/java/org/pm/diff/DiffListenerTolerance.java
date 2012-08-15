package org.pm.diff;

import java.util.HashMap;
import java.lang.Math;

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
