package uk.co.firstzero.csv

import java.io.File;

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class CsvToExcelTask extends DefaultTask  {
    @TaskAction
    def read() {
		def args = project.csvToExcelArgs
		
		CsvToExcel csv = new CsvToExcel(args.inputFiles.files as String[], args.outputFile, (char)args.separator[0]);
		csv.execute()
    }
}