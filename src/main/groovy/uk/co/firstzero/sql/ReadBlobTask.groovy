package uk.co.firstzero.sql

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class ReadBlobTask extends DefaultTask  {
    @TaskAction
    def read() {
        def antReadBlob = new AntReadBlob()

        antReadBlob.setClassName((String)project.readBlob.className)
        antReadBlob.setJdbcUrl((String)project.readBlob.jdbcUrl)
        antReadBlob.setUser((String)project.readBlob.user)
        antReadBlob.setPassword((String)project.readBlob.password)
        antReadBlob.setExtension((String)project.readBlob.extension)
        antReadBlob.setSql((String)project.readBlob.sql)
        antReadBlob.setOutputDirectory((String)project.readBlob.outputDirectory)
        antReadBlob.setUnzip((boolean)project.readBlob.unzip)

        antReadBlob.execute()
    

    }
}