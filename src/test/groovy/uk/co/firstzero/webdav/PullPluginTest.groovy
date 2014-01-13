package uk.co.firstzero.webdav

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test
import uk.co.firstzero.webdav.PullTask
import uk.co.firstzero.PullPluginExtension

import static org.junit.Assert.assertTrue

class PullPluginTest {
    @Test
    public void readBlobPluginAddsReadBlobTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'addonjavaanttasks'
        assertTrue(project.tasks.pullTask instanceof PullTask)
    }

    @Test
    public void readBlobPluginAddsReadBlobArgsToProject() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'addonjavaanttasks'
        assertTrue(project.extensions.pullArgs instanceof PullPluginExtension)
    }
}
