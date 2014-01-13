package uk.co.firstzero.csv

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.file.FileTree

class CsvToExcelPluginExtension {
    String outputFile
	FileTree inputFiles
	String separator = ','
}

class CsvToExcelPlugin implements Plugin<Project> {
    void apply(Project target) {
        target.extensions.create('csvToExcelArgs', CsvToExcelPluginExtension)
        target.task('csvToExcelTask', description:'Converts a set of csv files into 1 Excel file. Each csv file is a sheet within excel', type: CsvToExcelTask)
    }
}
