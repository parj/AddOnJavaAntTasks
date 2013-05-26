package uk.co.firstzero.webdav

import org.gradle.api.Plugin
import org.gradle.api.Project

class PullPluginExtension {
    String user;
    String password;
    String url;
    String file;
    String outFile;
    String proxyUser;
    String proxyPassword;
    String proxyHost;
    boolean overwrite;
    int proxyPort;
}

class PullPlugin implements Plugin<Project> {
    void apply(Project target) {
        target.extensions.create('pull', PullPluginExtension)
        target.task('pullTask', type: PullTask)
    }
}
