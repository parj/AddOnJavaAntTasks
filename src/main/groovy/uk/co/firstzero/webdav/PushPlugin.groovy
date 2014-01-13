package uk.co.firstzero.webdav

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileTree

class PushPluginExtension {
    String user
    String password
    String url
    String proxyUser
    String proxyPassword
    String proxyHost
    int proxyPort
    boolean overwrite
    boolean createDirectoryStructure
    FileTree tree
}

class PushPlugin implements Plugin<Project> {
    void apply(Project target) {
        target.extensions.create('pushArgs', PushPluginExtension)
        target.task('pushTask', description:'Pushes files to a WEBDAV site, proxy configuration is supported', type: PushTask)
    }
}
