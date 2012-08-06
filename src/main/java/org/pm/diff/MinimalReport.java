package org.pm.diff;

import java.io.IOException;

public class MinimalReport implements Report {
	private int numberOfDifferences = 0;
	
	public MinimalReport() throws IOException {
	}
	
	public void write(Difference difference) {
		++numberOfDifferences;
		System.out.println(difference.toString());
	}
	
	public void close() throws IOException {

	}

	@Override
	public void open(String fileName) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public int getNumberOfDifferences() {
		return numberOfDifferences;
	}

}
