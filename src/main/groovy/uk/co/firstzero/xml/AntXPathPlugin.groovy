package uk.co.firstzero.xml

import java.util.List;

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileTree

class AntXPathPluginExtension {
    String renamePattern	//Example //date or //date#_#//price
    FileTree inputDirectory
    String outputDirectory
	List<ModifyPath> modifyPaths
}

class AntXPathPlugin implements Plugin<Project> {
    void apply(Project target) {
        target.extensions.create('antXPathArgs', AntXPathPluginExtension)
        target.task('antXPathTask', description:'Strips out fluff from the XML and manipulating the XML. The use case for this is, before comparison, sometimes XMLs need to be cleaned and renamed. The cleaned xmls can be diffed using xmlunittask.', type: AntXPathTask)
    }
}
