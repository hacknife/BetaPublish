package com.iwdael.betapublish


import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.TaskAction

class InfoTask extends BaseTask {

    @TaskAction
    void run() throws Exception {
        BetaExtension extension = project.getExtensions().findByType(BetaExtension.class);
        PropertyFinder finder = new PropertyFinder(project, extension)
        finder.invalid()
        project.logger.log(LogLevel.WARN, "beta config: {")
        project.logger.log(LogLevel.WARN, "     apiKey: ${finder.apiKey}")
        project.logger.log(LogLevel.WARN, "     file: ${finder.file}")
        project.logger.log(LogLevel.WARN, "     installType: ${finder.installType}")
        project.logger.log(LogLevel.WARN, "     password: ${finder.password}")
        project.logger.log(LogLevel.WARN, "     updateDescription: ${finder.updateDescription}")
        project.logger.log(LogLevel.WARN, "}")
    }

}
