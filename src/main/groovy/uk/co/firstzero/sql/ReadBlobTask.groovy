package uk.co.firstzero.sql

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class ReadBlobTask extends DefaultTask  {
    @TaskAction
    def read() {
        antReadBlob = new AntReadBlob(
                (String) ${project.readBlobPluginArgs.className},
                (String) ${project.readBlobPluginArgs.jdbcUrl},
                (String) ${project.readBlobPluginArgs.user},
                (String) ${project.readBlobPluginArgs.password},
                (String) ${project.readBlobPluginArgs.extension},
                (String) ${project.readBlobPluginArgs.sql},
                (String) ${project.readBlobPluginArgs.outputDirectory},
                (boolean)${project.readBlobPluginArgs.unzip})

        antReadBlob.execute()
    }
}
