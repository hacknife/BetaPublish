package com.iwdael.betapublish


import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.ProjectConfigurationException

class BetaPublishPlugin implements Plugin<Project> {
    public static final String sPluginExtensionName = "beta"
    public static final String groupReinforce = "Beta Publish"

    @Override
    void apply(Project project) {
        if (!project.plugins.hasPlugin("com.android.application")) {
            throw new ProjectConfigurationException("Plugin requires the 'com.android.application' plugin to be configured.", null);
        }
        project.extensions.create(sPluginExtensionName, BetaExtension, project)
        createReinforceTask(project)
        createInfoTask(project)
    }


    static def createReinforceTask(Project project) {
        project.tasks.create('publish pgyer', PublishTask) {
            group groupReinforce
        }
    }

    static def createInfoTask(Project project) {
        project.tasks.create('info', InfoTask) {
            group groupReinforce
        }
    }
}

