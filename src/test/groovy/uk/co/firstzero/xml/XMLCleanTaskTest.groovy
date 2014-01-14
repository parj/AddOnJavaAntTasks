package uk.co.firstzero.xml

import org.codehaus.groovy.tools.shell.util.Logger
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before

import static org.junit.Assert.*

import org.gradle.api.Project
import org.junit.Test
import org.apache.log4j.Logger

class XMLCleanTaskTest {
    def logger = Logger.getLogger(XMLCleanTaskTest.class)
    def DIRECTORY_INPUT  = System.getProperty("user.dir") + '/src/test/resources/xml/AntXPathTest'
    def DIRECTORY_OUTPUT = System.getProperty("user.dir") + '/src/test/resources/xml/AntXPathTest'
    def FILE_TO_CHECK = DIRECTORY_OUTPUT + File.separator + "2000-12-16_5.95.xml"

    @Before
    void preSetup() {
        if (new File(FILE_TO_CHECK).exists())
            new File(FILE_TO_CHECK).delete()
    }

    @Test
    public void canAddXMLCleanTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        def task = project.task('xmlCleanTaskTest', type: XMLCleanTask)
        assertTrue(task instanceof XMLCleanTask)
    }

    @Test
    public void xmlCleanTaskTest() {
        Project project = ProjectBuilder.builder().build()

        logger.debug "About to apply plugin"
        project.apply plugin: 'addonjavaanttasks'

        def task = project.task('xmlCleanTaskTest', type: XMLCleanTask)
        def args = project.extensions.xmlCleanArgs

        logger.debug "DIRECTORY_INPUT  = $DIRECTORY_INPUT"
        logger.debug "DIRECTORY_OUTPUT = $DIRECTORY_OUTPUT"
        logger.debug "FILE_TO_CHECK    = $FILE_TO_CHECK"

        args.inputDirectory = project.fileTree(dir: DIRECTORY_INPUT, include: '*.xml')
        args.outputDirectory = DIRECTORY_OUTPUT
        args.renamePattern = "//book[@id='bk102']/publish_date#_#//book[@id='bk102']/price"
        args.modifyPaths = [ new uk.co.firstzero.xml.ModifyPath(path: "//title", delete: true),
                             new uk.co.firstzero.xml.ModifyPath(path: "//author", value:"ToDo")]

        task.execute()
        assertEquals(new File(FILE_TO_CHECK).exists(), true);
    }
}
