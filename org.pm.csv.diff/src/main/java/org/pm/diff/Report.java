package org.pm.diff;

import java.io.IOException;

public interface Report {
	public void open(String fileName) throws IOException;
	public void close() throws IOException;
	public void write(Difference difference) throws IOException;
	public String toString();
}
