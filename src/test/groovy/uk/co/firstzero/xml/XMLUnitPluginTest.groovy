package uk.co.firstzero.xml

import org.junit.Test
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import static org.junit.Assert.*

class XMLUnitPluginTest {
    @Test
    public void xmlUnitPluginAddsXMLUnitTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'xmlunit'
        assertTrue(project.tasks.xmlUnitTask instanceof XMLUnitTask)
    }

    @Test
    public void xmlUnitPluginAddsXMLUnitArgsToProject() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'xmlunit'
        assertTrue(project.extensions.xmlUnitArgs instanceof XMLUnitPluginExtension)
    }
}
