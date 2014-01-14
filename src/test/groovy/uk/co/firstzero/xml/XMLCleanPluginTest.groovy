package uk.co.firstzero.xml

import org.junit.Test
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import uk.co.firstzero.XMLCleanPluginExtension
import static org.junit.Assert.*

class XMLCleanPluginTest {
    @Test
    public void xmlCleanPluginAddsXmlCleanTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'addonjavaanttasks'
        assertTrue(project.tasks.xmlCleanTask instanceof XMLCleanTask)
    }

    @Test
    public void xmlCleanPluginAddsXMLCleanArgsToProject() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'addonjavaanttasks'
        assertTrue(project.extensions.xmlCleanArgs instanceof XMLCleanPluginExtension)
    }
}
