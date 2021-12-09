package com.iwdael.betapublish

import groovy.json.JsonOutput
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.TaskAction
class PublishTask extends BaseTask {
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

        Map<String, String> map = new HashMap<>()
        map.put("_api_key", finder.apiKey)
        map.put("buildInstallType", finder.installType)
        map.put("buildPassword", finder.password)
        map.put("buildUpdateDescription", finder.updateDescription)
        upload(map, finder.file, { project.logger.warn(it) }, {
            project.logger.log(LogLevel.WARN, "")
            project.logger.log(LogLevel.WARN, JsonOutput.prettyPrint(it))
        })

    }
}
