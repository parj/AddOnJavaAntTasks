package uk.co.firstzero.webdav

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.apache.log4j.Logger;

class PullTask extends DefaultTask  {
    /**
     * Downloads files from a WEBDAV site, proxy configuration is supported
     */
    @TaskAction
    void pullAction() {
        def antPull = new uk.co.firstzero.webdav.Pull()

        antPull.setUser((String)project.pullArgs.user)
        antPull.setPassword((String)project.pullArgs.password)
        antPull.setUrl((String)project.pullArgs.url)
        antPull.setFile((String)project.pullArgs.file)
        antPull.setOutFile((String)project.pullArgs.outFile)
        antPull.setOverwrite((boolean)project.pullArgs.overwrite)

        if (project.pullArgs.proxyUser != null)
            antPull.setProxyUser((String)project.pullArgs.proxyUser)

        if (project.pullArgs.proxyPassword != null)
            antPull.setProxyPassword((String)project.pullArgs.proxyPassword)

        if (project.pullArgs.proxyHost != null)
            antPull.setProxyHost((String)project.pullArgs.proxyHost)

        if (project.pullArgs.proxyPort != null)
            antPull.setProxyPort((int)project.pullArgs.proxyPort)

        antPull.execute()
    }
}
    

