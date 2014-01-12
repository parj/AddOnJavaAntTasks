package uk.co.firstzero.csv

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import uk.co.firstzero.diff.CsvReport;
import uk.co.firstzero.diff.Report;

class CsvDiffTask extends DefaultTask  {
    @TaskAction
    def diff() {
        def antCsv = new AntCsvDiff()
		def args = project.csvDiffArgs
		
		File testDir = new File(args.testDirectory)
        if (!testDir.exists()) {
            logger.error(args.testDirectory + " does not exist")
            println(args.testDirectory + " does not exist - skipping")
        } else {
            args.controlDirectory.each {File controlFile ->
				File testFile = new File(args.testDirectory + File.separator + controlFile.getName())
				Report report = new CsvReport(args.resultDirectory + "/report_" + name + ".csv")
				
				CsvDiff csv = new CsvDiff(controlFile.getCanonicalPath(), testFile.getCanonicalPath(), (char)args.separator[0])
				csv.setReport(report)
				csv.setKeyColumns(args.keyColumns)
				csv.diff()
                
				logger.debug("Diff of " + controlFile.getName() + " completed ");
            }
        }
    

    }
}