package uk.co.firstzero.xml;

import org.junit.Test
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import static org.junit.Assert.*

import org.gradle.api.Project
import org.junit.Test;

class XMLDiffTaskTest {

	@Test
    public void canAddXMLUnitTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        def task = project.task('xmlDiffTaskTest', type: XMLDiffTask)
        assertTrue(task instanceof XMLDiffTask)
    }
}
