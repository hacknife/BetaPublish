package com.iwdael.install.http

class Response<T> {
    var code = 0
    var message: String? = null
    var data: T? = null
}