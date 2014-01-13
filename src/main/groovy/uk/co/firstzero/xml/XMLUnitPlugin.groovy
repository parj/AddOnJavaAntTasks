package uk.co.firstzero.xml

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileTree

class XMLUnitPluginExtension {
    String resultDirectory;
    String separator = ","
    FileTree controlDirectory
    String testDirectory
}

class XMLUnitPlugin implements Plugin<Project> {
    void apply(Project target) {
        target.extensions.create('xmlUnitArgs', XMLUnitPluginExtension)
        target.task('xmlUnitTask', description:'Diffs two directories containing xml files. Each directory must have the same name and number of files as the other', type: XMLUnitTask)
    }
}
