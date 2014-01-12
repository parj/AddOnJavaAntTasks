package uk.co.firstzero.webdav

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import static org.junit.Assert.assertTrue

class PushPluginTest {
    @Test
    public void pushPluginAddsPushTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'push'
        assertTrue(project.tasks.pushTask instanceof PushTask)
    }

    @Test
    public void pushPluginAddsPushArgsToProject() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'push'
        assertTrue(project.extensions.pushArgs instanceof PushPluginExtension)
    }
}
