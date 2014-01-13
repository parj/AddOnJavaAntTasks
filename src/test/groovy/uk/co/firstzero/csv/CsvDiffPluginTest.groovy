package uk.co.firstzero.csv

import org.junit.Test
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import uk.co.firstzero.CsvDiffPluginExtension
import static org.junit.Assert.*

class CsvDiffPluginTest {
    @Test
    public void csvDiffPluginAddsCsvDiffTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'addonjavaanttasks'
        assertTrue(project.tasks.csvDiffTask instanceof CsvDiffTask)
    }

    @Test
    public void readBlobPluginAddsReadBlobArgsToProject() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'addonjavaanttasks'
        assertTrue(project.extensions.csvDiffArgs instanceof CsvDiffPluginExtension)
    }
}
