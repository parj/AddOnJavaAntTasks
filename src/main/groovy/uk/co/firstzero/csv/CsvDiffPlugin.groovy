package uk.co.firstzero.csv

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.file.FileTree;

class CsvDiffPluginExtension {
	String keyColumns
	FileTree controlDirectory
	String testDirectory
	String resultDirectory
	String separator = ','	
}

class CsvDiffPlugin implements Plugin<Project> {
    void apply(Project target) {
        target.extensions.create('csvDiffArgs', CsvDiffPluginExtension)
        target.task('csvDiffTask', type: CsvDiffTask)
    }
}
