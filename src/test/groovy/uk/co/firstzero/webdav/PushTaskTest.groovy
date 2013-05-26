package uk.co.firstzero.webdav

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import static org.junit.Assert.assertTrue

class PushTaskTest {
    @Test
    public void canAddTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        def task = project.task('pushTask', type: PushTask)
        assertTrue(task instanceof PushTask)
    }
}
