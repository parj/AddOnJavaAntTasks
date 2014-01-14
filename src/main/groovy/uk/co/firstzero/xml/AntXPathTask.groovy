package uk.co.firstzero.xml

import org.custommonkey.xmlunit.Difference
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class AntXPathTask extends DefaultTask  {

    /**
     * Strips out fluff from the XML and manipulating the XML. The use case for this is, before comparison, sometimes XMLs need to be cleaned and renamed. The cleaned xmls can be diffed using xmlunittask.
     * @return Nothing
     */
    @TaskAction
    void diffXMLAction() {
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
    

