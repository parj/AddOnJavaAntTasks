package uk.co.firstzero.sql

import org.gradle.api.Project
import org.gradle.api.Plugin

class ReadBlobPluginExtension {
    String className;
    String jdbcUrl;
    String user;
    String password;
    String extension;
    String sql;
    String outputDirectory;
    boolean unzip;
}

class ReadBlobPlugin implements Plugin<Project> {
    void apply(Project target) {
        target.extensions.create('readBlobArgs', ReadBlobPluginExtension)
        target.task('readBlobTask', description: 'Extracts Blobs from Database.SQL should contain a string name and then blob', type: ReadBlobTask)
    }
}
