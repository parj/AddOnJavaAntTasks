package uk.co.firstzero.xml

import org.apache.log4j.Logger
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before

import static org.junit.Assert.*

import org.gradle.api.Project
import org.junit.Test;

class XMLDiffTaskTest {
    def logger = Logger.getLogger(XMLDiffTaskTest.class);
    def DIRECTORY_OUTPUT = System.getProperty("user.dir") + "/src/test/resources/xml/AntXMLUnitTest"
    def CONTROL = System.getProperty("user.dir") + "/src/test/resources/xml/AntXMLUnitTest/control"
    def TEST = System.getProperty("user.dir") + '/src/test/resources/xml/AntXMLUnitTest/test'
    def FILE_TO_CHECK = DIRECTORY_OUTPUT + File.separator + "diffxml.xml.csv"

    @Before
    void preSetup() {
        if (new File(FILE_TO_CHECK).exists())
            new File(FILE_TO_CHECK).delete()
    }

	@Test
    public void canAddXMLUnitTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        def task = project.task('xmlDiffTaskTest', type: XMLDiffTask)
        assertTrue(task instanceof XMLDiffTask)
    }

    @Test
    public void xmlDiffTaskTest() {
        Project project = ProjectBuilder.builder().build()
        logger.debug "About to apply plugin"
        project.apply plugin: 'addonjavaanttasks'

        def task = project.task('xmlDiffTaskTest', type: XMLDiffTask)
        def args = project.extensions.xmlDiffArgs

        logger.debug "DIRECTORY_OUTPUT = $DIRECTORY_OUTPUT"
        logger.debug "CONTROL = $CONTROL"
        logger.debug "TEST = $TEST"

        args.resultDirectory = DIRECTORY_OUTPUT
        args.separator = ","
        args.controlDirectory = project.fileTree(dir: CONTROL, include: '*.xml')
        args.testDirectory = TEST

        task.execute()
        assertEquals(new File(FILE_TO_CHECK).exists(), true);
    }
}
