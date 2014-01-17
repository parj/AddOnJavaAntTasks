package uk.co.firstzero.csv

import org.apache.log4j.Logger
import org.junit.Test
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import static org.junit.Assert.*

class CsvDiffTaskTest {
    def logger = Logger.getLogger(CsvToExcelTaskTest.class)
    def DIRECTORY_CONTROL  = System.getProperty("user.dir") + '/src/test/resources/csv/CsvDiff/control'
    def DIRECTORY_TEST     = System.getProperty("user.dir") + '/src/test/resources/csv/CsvDiff/test'
    def DIRECTORY_OUTPUT = System.getProperty("user.dir") + '/src/test/resources/csv/CsvDiff'

    @Test
    public void canAddTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        def task = project.task('csvDiffTaskTest', type: CsvDiffTask)
        assertTrue(task instanceof CsvDiffTask)
    }

    @Test
    public void ExecuteCsvDiffTask() {
        Project project = ProjectBuilder.builder().build()

        logger.debug "About to apply plugin"
        project.apply plugin: 'addonjavaanttasks'

        def task = project.task('csvDiffTaskTest', type: CsvDiffTask)
        def args = project.extensions.csvDiffArgs

        args.resultDirectory = DIRECTORY_OUTPUT
        args.separator = ","
        args.controlDirectory = project.fileTree(dir: DIRECTORY_CONTROL, include: '*.csv')
        args.testDirectory = DIRECTORY_TEST
        args.keyColumns = 'Header_1'

        task.execute()

        assertEquals(new File(DIRECTORY_OUTPUT + "/report_csvDiffTaskTest.csv").exists(), true);
    }
}
