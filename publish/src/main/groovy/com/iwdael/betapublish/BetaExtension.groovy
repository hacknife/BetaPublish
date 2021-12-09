package com.iwdael.betapublish

import org.gradle.api.Project


class BetaExtension {
    String apiKey
    String file
    String installType
    String password
    String updateDescription

    BetaExtension(Project project) {
    }
}
