package com.iwdael.betapublish


import org.gradle.api.GradleException
import org.gradle.api.Project;

class PropertyFinder {

    private final Project project
    private final BetaExtension extension


    PropertyFinder(Project project, BetaExtension extension) {
        this.extension = extension
        this.project = project
    }

    def getApiKey() {
        getString(project, 'apiKey', extension.apiKey)
    }

    def getFile() {
        new File(project.parent.projectDir, getString(project, 'file', extension.file))
    }

    def getInstallType() {
        getString(project, 'installType', extension.installType)
    }

    def getPassword() {
        getString(project, 'password', extension.password)
    }

    def getUpdateDescription() {
        new String(getString(project, 'updateDescription', extension.updateDescription).getBytes("UTF-8") ,"GBK")
    }


    private String getString(Project project, String propertyName, String defaultValue) {
        project.hasProperty(propertyName) ? project.getProperty(propertyName) : defaultValue
    }

    private boolean getBoolean(Project project, String propertyName, boolean defaultValue) {
        project.hasProperty(propertyName) ? Boolean.parseBoolean(project.getProperty(propertyName)) : defaultValue
    }

    void invalid() {
        if (apiKey == null) {
            throw new GradleException("Missing attributes: beta.apiKey")
        }
        if (file == null) {
            throw new GradleException("Missing attributes: beta.file")
        }
        if (installType == null) {
            throw new GradleException("Missing attributes: beta.installType")
        }
        if (password == null) {
            throw new GradleException("Missing attributes: beta.password")
        }
        if (updateDescription == null) {
            throw new GradleException("Missing attributes: beta.updateDescription")
        }
    }
}
