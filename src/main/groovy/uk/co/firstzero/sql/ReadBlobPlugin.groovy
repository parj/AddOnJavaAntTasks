package uk.co.firstzero.sql

import org.gradle.api.Project
import org.gradle.api.Plugin

class ReadBlobPluginExtension {
    private String className;
    private String jdbcUrl;
    private String user;
    private String password;
    private String extension;
    private String sql;
    private String outputDirectory;
    private boolean unzip;
}

class ReadBlobPlugin implements Plugin<Project> {
    void apply(Project target) {
        target.extensions.create('readBlob', ReadBlobPluginExtension)
        target.task('readBlobTask', type: ReadBlobTask)
    }
}
