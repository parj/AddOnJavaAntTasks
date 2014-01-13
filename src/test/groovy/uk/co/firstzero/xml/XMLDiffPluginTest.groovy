package uk.co.firstzero.xml

import org.junit.Test
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import uk.co.firstzero.XMLDiffPluginExtension
import static org.junit.Assert.*

class XMLDiffPluginTest {
    @Test
    public void xmlUnitPluginAddsXMLUnitTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'addonjavaanttasks'
        assertTrue(project.tasks.xmlDiffTask instanceof XMLDiffTask)
    }

    @Test
    public void xmlUnitPluginAddsXMLUnitArgsToProject() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'addonjavaanttasks'
        assertTrue(project.extensions.xmlDiffArgs instanceof XMLDiffPluginExtension)
    }
}
