package uk.co.firstzero.csv

import java.io.File;

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class CsvToExcelTask extends DefaultTask  {

    /**
     * Reads in CSV files and converts into an Excel document. Each file is 1 sheet in the Excel document
     */
    @TaskAction
    void read() {
		def args = project.csvToExcelArgs
		
		CsvToExcel csv = new CsvToExcel(args.inputFiles.files as String[], args.outputFile, (char)args.separator[0]);
		csv.execute()
    }
}