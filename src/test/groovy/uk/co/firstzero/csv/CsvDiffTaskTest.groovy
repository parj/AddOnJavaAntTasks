package uk.co.firstzero.csv

import org.junit.Test
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import static org.junit.Assert.*

class CsvDiffTaskTest {
    @Test
    public void canAddTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        def task = project.task('csvDiffTaskTest', type: CsvDiffTask)
        assertTrue(task instanceof CsvDiffTask)
    }
}
