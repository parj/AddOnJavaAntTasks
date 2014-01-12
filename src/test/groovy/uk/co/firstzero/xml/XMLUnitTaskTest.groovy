package uk.co.firstzero.xml;

import org.junit.Test
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import static org.junit.Assert.*

import org.gradle.api.Project
import org.junit.Test;

class XMLUnitTaskTest {

	@Test
    public void canAddXMLUnitTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        def task = project.task('xmlUnitTaskTest', type: XMLUnitTask)
        assertTrue(task instanceof XMLUnitTask)
    }
}
