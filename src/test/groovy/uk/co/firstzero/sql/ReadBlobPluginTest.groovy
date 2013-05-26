package uk.co.firstzero.sql

import org.junit.Test
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import static org.junit.Assert.*

class ReadBlobPluginTest {
    @Test
    public void readBlobPluginAddsReadBlobTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'readblob'
        assertTrue(project.tasks.readBlobTask instanceof ReadBlobTask)
    }

    @Test
    public void readBlobPluginAddsReadBlobArgsToProject() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'readblob'
        assertTrue(project.extensions.readBlobArgs instanceof ReadBlobPluginExtension)
    }
}
