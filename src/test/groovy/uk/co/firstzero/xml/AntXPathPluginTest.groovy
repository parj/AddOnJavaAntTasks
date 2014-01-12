package uk.co.firstzero.xml

import org.junit.Test
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import static org.junit.Assert.*

class AntXPathPluginTest {
    @Test
    public void antXPathPluginAddsAntXPathTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'antxpath'
        assertTrue(project.tasks.antXPathTask instanceof AntXPathTask)
    }

    @Test
    public void antXPathPluginAddsAntXPathArgsToProject() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'antxpath'
        assertTrue(project.extensions.antXPathArgs instanceof AntXPathPluginExtension)
    }
}
