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
        target.task('antXPathTask', type: AntXPathTask)
    }
}
