package uk.co.firstzero.sql

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import static org.junit.Assert.*

class ReadBlobTaskTest {
    AntReadBlobTest readBlobTest = new AntReadBlobTest();

    @Before
    void setUp() {
        readBlobTest.setUp()
    }

    @Test
    public void canAddReadBlobTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        def task = project.task('readBlobTaskTest', type: ReadBlobTask)
        assertTrue(task instanceof ReadBlobTask)
    }

    @Test
    public void readBlobTaskTest() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'addonjavaanttasks'

        project.extensions.readBlobArgs.className = readBlobTest.CLASS_NAME
        project.extensions.readBlobArgs.jdbcUrl = readBlobTest.JDBC_URL
        project.extensions.readBlobArgs.user = readBlobTest.USER
        project.extensions.readBlobArgs.password = readBlobTest.PASSWORD
        project.extensions.readBlobArgs.extension = '.groovy.jpg'
        project.extensions.readBlobArgs.sql = readBlobTest.SQL_EXTRACTION
        project.extensions.readBlobArgs.outputDirectory = readBlobTest.DIRECTORY_OUTPUT
        project.extensions.readBlobArgs.unzip = true

        def task = project.task('readBlobTaskTest', type: ReadBlobTask)
        task.execute()

        assertEquals(new File(readBlobTest.DIRECTORY_OUTPUT + File.separator + "blob.zip.groovy.jpg").exists(), true);
    }

    @After
    void tearDown() {
        readBlobTest.tearDown()
    }
}
