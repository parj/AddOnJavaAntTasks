package uk.co.firstzero.xml

import org.custommonkey.xmlunit.Difference
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class XMLCleanTask extends DefaultTask  {

    /**
     * Strips out fluff from the XML and manipulating the XML. The use case for this is, before comparison, sometimes XMLs need to be cleaned and renamed. The cleaned xmls can be diffed using xmlunittask.
     * @return Nothing
     */
    @TaskAction
    void xmlClean() {
		//Setup
        def antXPathObject = new AntXPath();
        def args = project.xmlCleanArgs

		antXPathObject.setOutputDirectory((String)args.outputDirectory)
		antXPathObject.setRenamePattern((String)args.renamePattern)
		antXPathObject.setModifyPaths((List<ModifyPath>)args.modifyPaths)
		
        antXPathObject.preSetup()

        args.inputDirectory.each {File inputFile ->
			antXPathObject.processFile(inputFile)
            logger.debug("Completed " + inputFile.getName());
           
        }
    }
}
    

