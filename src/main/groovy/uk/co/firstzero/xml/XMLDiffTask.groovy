package uk.co.firstzero.xml

import org.custommonkey.xmlunit.Difference
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class XMLDiffTask extends DefaultTask  {

    /**
     * Diffs two directories containing xml files. Each directory must have the same name and number of files as the other
     * @return Nothing
     */
    @TaskAction
    void diffXMLAction() {
		def args = project.xmlDiffArgs
		
		//Setup
		def xmlUnitObject = new AntXMLUnit();
		xmlUnitObject.setResultDirectory((String)args.resultDirectory)
		xmlUnitObject.setSeparator((String)args.separator)
		
        File testDir = new File(args.testDirectory)
        if (!testDir.exists()) {
            logger.error(testDirectory + " does not exist")
            println(testDirectory + " does not exist - skipping")
        } else {
            xmlUnitObject.preSetup()

            args.controlDirectory.each {File controlFile ->
				File testFile = new File(args.testDirectory + File.separator + controlFile.getName())
                List<Difference> differences = xmlUnitObject.processFile(controlFile, testFile);
                xmlUnitObject.writeReport(differences, controlFile);
                logger.debug("Diff of " + controlFile.getName() + " completed ");
            }
        }
    }
}
    

