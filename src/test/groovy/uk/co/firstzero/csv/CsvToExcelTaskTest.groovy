package uk.co.firstzero.csv

import org.apache.log4j.Logger
import org.junit.Test
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import static org.junit.Assert.*

class CsvToExcelTaskTest {
    def logger = Logger.getLogger(CsvToExcelTaskTest.class)
    def DIRECTORY_INPUT  = System.getProperty("user.dir") + '/src/test/resources/csv/CsvToExcel'
    def OUTPUT_FILE = System.getProperty("user.dir") + '/src/test/resources/csv/CsvToExcel/report.xls'

    @Test
    public void canAddTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        def task = project.task('csvToExcelTaskTest', type: CsvToExcelTask)
        assertTrue(task instanceof CsvToExcelTask)
    }

    @Test
    public void ExecuteCsvToExcelTask() {
        Project project = ProjectBuilder.builder().build()

        logger.debug "About to apply plugin"
        project.apply plugin: 'addonjavaanttasks'

        def task = project.task('csvToExcelTaskTest', type: CsvToExcelTask)
        def args = project.extensions.csvToExcelArgs

        args.inputFiles = project.fileTree(dir: DIRECTORY_INPUT, include: 'output.csv')
        args.outputFile = OUTPUT_FILE
        args.separator = ','

        task.execute()

        assertEquals(new File(OUTPUT_FILE).exists(), true);
    }
}
