package uk.co.firstzero.xml

import org.custommonkey.xmlunit.Difference
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class AntXPathTask extends DefaultTask  {

    @TaskAction
    def diffXMLAction() {
		//Setup
        def antXPathObject = new AntXPath();
		antXPathObject.setOutputDirectory((String)project.antXPathArgs.outputDirectory)
		antXPathObject.setRenamePattern((String)project.antXPathArgs.renamePattern)
		antXPathObject.setModifyPaths((List<ModifyPath>)project.antXPathArgs.modifyPaths)
		
        antXPathObject.preSetup()

        project.antXPathArgs.inputDirectory.each {File inputFile ->
			antXPathObject.processFile(inputFile)
            logger.debug("Completed " + inputFile.getName());
           
        }
    }
}
    

