package com.hacknife.install

import android.content.Context

class Pgyer {
  lateinit  var context: Context
    lateinit    var _api_key: String
    lateinit  var appKey: String
    lateinit   var versionName: String

    companion object {
        val pgyer = Pgyer()
    }
}