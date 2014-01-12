package uk.co.firstzero.xml

import org.custommonkey.xmlunit.Difference
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class XMLUnitTask extends DefaultTask  {

    @TaskAction
    def diffXMLAction() {
		//Setup
        def xmlUnitObject = new AntXMLUnit();
		xmlUnitObject.setResultDirectory((String)project.xmlUnitArgs.resultDirectory)
		xmlUnitObject.setSeparator((String)project.xmlUnitArgs.separator)
		
        File testDir = new File(project.xmlUnitArgs.testDirectory)
        if (!testDir.exists()) {
            logger.error(testDirectory + " does not exist")
            println(testDirectory + " does not exist - skipping")
        } else {
            xmlUnitObject.preSetup()

            project.xmlUnitArgs.controlDirectory.each {File controlFile ->
				File testFile = new File(project.xmlUnitArgs.testDirectory + File.separator + controlFile.getName())
                List<Difference> differences = xmlUnitObject.processFile(controlFile, testFile);
                xmlUnitObject.writeReport(differences, controlFile);
                logger.debug("Diff of " + controlFile.getName() + " completed ");
            }
        }
    }
}
    

