package uk.co.firstzero.webdav

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import uk.co.firstzero.webdav.Pull

class PullTask extends DefaultTask  {
    @TaskAction
    def pullAction() {
        def pull = new Pull()

        pull.setUser((String)${project.pull.user})
        pull.setPassword((String)${project.pull.password})
        pull.setUrl((String)${project.pull.url})
        pull.setFile((String)${project.pull.file})
        pull.setOutFile((String)${project.pull.outFile})

        if (${project.pull.proxyUser} != null)
            pull.setProxyUser((String)${project.pull.proxyUser})

        if (${project.pull.proxyPassword} != null)
            pull.setProxyPassword((String)${project.pull.proxyPassword})

        if (${project.pull.proxyHost} != null)
            pull.setProxyHost((String)${project.pull.proxyHost})

        if (${project.pull.proxyPort} != null)
            pull.setProxyPort((String)${project.pull.proxyPort})

        pull.execute()
    }
}
