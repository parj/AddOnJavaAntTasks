package uk.co.firstzero.sql

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class ReadBlobTask extends DefaultTask  {
    @TaskAction
    def read() {
        def antReadBlob = new AntReadBlob()

        antReadBlob.setClassName((String)project.readBlobArgs.className)
        antReadBlob.setJdbcUrl((String)project.readBlobArgs.jdbcUrl)
        antReadBlob.setUser((String)project.readBlobArgs.user)
        antReadBlob.setPassword((String)project.readBlobArgs.password)
        antReadBlob.setExtension((String)project.readBlobArgs.extension)
        antReadBlob.setSql((String)project.readBlobArgs.sql)
        antReadBlob.setOutputDirectory((String)project.readBlobArgs.outputDirectory)
        antReadBlob.setUnzip((boolean)project.readBlobArgs.unzip)

        antReadBlob.execute()
    

    }
}