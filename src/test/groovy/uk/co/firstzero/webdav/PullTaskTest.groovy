package uk.co.firstzero.webdav

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test
import uk.co.firstzero.webdav.PullTask

import static org.junit.Assert.assertTrue

class PullTaskTest {
    @Test
    public void canAddTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        def task = project.task('pullTask', type: PullTask)
        assertTrue(task instanceof PullTask)
    }
}
