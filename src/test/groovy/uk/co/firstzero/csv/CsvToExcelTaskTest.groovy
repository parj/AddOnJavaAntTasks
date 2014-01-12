package uk.co.firstzero.csv

import org.junit.Test
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import static org.junit.Assert.*

class CsvToExcelTaskTest {
    @Test
    public void canAddTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        def task = project.task('csvToExcelTaskTest', type: CsvToExcelTask)
        assertTrue(task instanceof CsvToExcelTask)
    }
}
