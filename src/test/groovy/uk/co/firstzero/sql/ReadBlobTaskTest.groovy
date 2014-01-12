package uk.co.firstzero.sql

import org.junit.Test
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import static org.junit.Assert.*

class ReadBlobTaskTest {
    @Test
    public void canAddTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        def task = project.task('readBlobTaskTest', type: ReadBlobTask)
        assertTrue(task instanceof ReadBlobTask)
    }
}
