package com.iwdael.install.http

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.iwdael.install.Pgyer.Companion.pgyer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import kotlin.Exception

/**
 * author : Iwdael
 * e-mail : iwdael@outlook.com
 * time   : 2019/8/5
 * desc   : MVVM
 * version: 1.0
 */

class HttpClient {
    companion object {
        fun check(): Version? {
            try {
                val url = "https://www.pgyer.com/apiv2/app/check?_api_key=${pgyer._api_key}&appKey=${pgyer.appKey}&buildVersion=${pgyer.versionName}"
                Log.v("dzq", "${url}")
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.connectTimeout = 3 * 60 * 1000
                connection.readTimeout = 3 * 60 * 1000
                connection.requestMethod = "POST"
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val inStream = connection.inputStream
                    val bytes = read(inStream)
                    val jsonStr = String(bytes)
                    val json = Gson().fromJson<Response<Version>>(jsonStr, object : TypeToken<Response<Version>>() {}.type)
                    return json?.data
                }
            } catch (e: Exception) {
                return null
            }
            return null
        }


        fun history(page: Int): List<Version>? {
            try {
                val url = "https://www.pgyer.com/apiv2/app/builds"
                Log.v("dzq", "${url}")
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.connectTimeout = 3 * 60 * 1000
                connection.readTimeout = 3 * 60 * 1000
                connection.requestMethod = "POST"
                connection.connect()
                val body = "_api_key=${pgyer._api_key}&appKey=${pgyer.appKey}&page=${page}"
                val outputStream = connection.outputStream
                outputStream.write(body.toByteArray(Charsets.UTF_8))
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val inStream = connection.inputStream
                    val bytes = read(inStream)
                    val jsonStr = String(bytes)
                    val json = Gson().fromJson<Response<Versions>>(jsonStr, object : TypeToken<Response<Versions>>() {}.type)
                    return json?.data?.list
                }
            } catch (e: Exception) {
                return null
            }
            return null
        }


        fun read(inStream: InputStream): ByteArray {
            val outStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var len = 0
            while (inStream.read(buffer).also { len = it } != -1) {
                outStream.write(buffer, 0, len)
            }
            inStream.close()
            return outStream.toByteArray()
        }

        fun download(context: Context, url: String, fileName: String, progress: ((Float) -> Unit), complete: ((File) -> Unit), error: (() -> Unit)) {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.connectTimeout = 3 * 60 * 1000
                connection.readTimeout = 3 * 60 * 1000
                connection.requestMethod = "POST"
                val file = File(context.cacheDir, fileName)
                file.deleteOnExit()
                val outputStream: OutputStream = FileOutputStream(file)
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val contentLength = connection.contentLength * 1f
                    val fileReader = ByteArray(1024)
                    var currentLength = 0
                    while (true) {
                        val read = inputStream.read(fileReader)
                        if (read == -1) break
                        currentLength += read
                        outputStream.write(fileReader, 0, read)
                        GlobalScope.launch(Dispatchers.Main) {
                            progress.invoke(100 * currentLength / contentLength)
                        }
                    }
                    outputStream.flush()
                    inputStream.close()
                    outputStream.close()
                }
                GlobalScope.launch(Dispatchers.Main) {
                    complete.invoke(file)
                }
            } catch (e: Exception) {
                error.invoke()
            }

        }

    }
}

