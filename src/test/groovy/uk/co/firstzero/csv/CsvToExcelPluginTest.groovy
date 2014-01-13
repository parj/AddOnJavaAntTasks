package uk.co.firstzero.csv

import org.junit.Test
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import uk.co.firstzero.CsvToExcelPluginExtension
import static org.junit.Assert.*

class CsvToExcelPluginTest {
    @Test
    public void csvToExcelPluginAddsCsvToExcelTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'addonjavaanttasks'
        assertTrue(project.tasks.csvToExcelTask instanceof CsvToExcelTask)
    }

    @Test
    public void csvToExcelPluginAddsCsvToExcelArgsToProject() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'addonjavaanttasks'
        assertTrue(project.extensions.csvToExcelArgs instanceof CsvToExcelPluginExtension)
    }
}
