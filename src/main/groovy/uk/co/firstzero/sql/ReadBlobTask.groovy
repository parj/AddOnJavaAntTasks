package uk.co.firstzero.sql

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class ReadBlobTask extends DefaultTask  {
    @TaskAction
    def read() {
        def antReadBlob = new AntReadBlob()

        antReadBlob.setClassName((String)${project.readBlobPluginArgs.className})
        antReadBlob.setJdbcUrl((String)${project.readBlobPluginArgs.jdbcUrl})
        antReadBlob.setUser((String)${project.readBlobPluginArgs.user})
        antReadBlob.setPassword((String)${project.readBlobPluginArgs.password})
        antReadBlob.setExtension((String)${project.readBlobPluginArgs.extension})
        antReadBlob.setSql((String)${project.readBlobPluginArgs.sql})
        antReadBlob.setOutputDirectory((String)${project.readBlobPluginArgs.outputDirectory})
        antReadBlob.setUnzip((boolean)${project.readBlobPluginArgs.unzip})

        antReadBlob.execute()
    }
}
