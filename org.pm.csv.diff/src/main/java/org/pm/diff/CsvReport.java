package org.pm.diff;

import java.io.FileWriter;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVWriter;

public class CsvReport implements Report {
	private CSVWriter writer;
	private String separator = ",";
	
	public CsvReport(String fileName) throws IOException {
		open(fileName);
	}
	
	public void open(String fileName) throws IOException {
		writer = new CSVWriter(new FileWriter(fileName), separator.charAt(0));
		writeHeader();
	}
	
	public void writeHeader() {
		String[] result = {
			"Line Number", 
			"Mismatch Type",
			"Expected", 
			"Reached", 
			"Difference"
		};
		writer.writeNext(result);
	}
	
	public void write(Difference difference) throws IOException {
		float expected;
		float reached;
		float diff;
		
		try {
			expected = new Float((String)difference.getExpected()).floatValue();
			reached = new Float((String)difference.getExpected()).floatValue();
			diff = expected - reached;
			
			String[] result = {
					difference.getLineNumber().toString(), 
					(String)difference.getExpected(),
					(String)difference.getReached(),
					new Float(diff).toString()
			};
			writer.writeNext(result);
		}	catch (Exception e) {
			//If the above is not a number
			String[] result = {
					difference.getLineNumber().toString(),
					difference.getMismatchType(),
					(String)difference.getExpected(),
					(String)difference.getReached()
			};
			writer.writeNext(result);
		}
		writer.flush();
	}
	
	public void close() throws IOException {
		if (writer != null) {
			writer.flush();
			writer.close();
		}
	}

	

}
