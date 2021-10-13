package com.hacknife.install.http

import com.google.gson.Gson
import com.hacknife.install.Install
import com.hacknife.install.Pgyer
import com.hacknife.install.Pgyer.Companion.pgyer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.*
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

/**
 * author : 段泽全(hacknife)
 * e-mail : hacknife@outlook.com
 * time   : 2019/8/5
 * desc   : MVVM
 * version: 1.0
 */

class HttpClient {
    companion object {
        fun check(): Version? {
            try {
                val url = "https://www.pgyer.com/apiv2/app/check?_api_key=${pgyer._api_key}&appKey=${pgyer.appKey}&buildVersion=${pgyer.versionName}"
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.connectTimeout = 3 * 60 * 1000
                connection.readTimeout = 3 * 60 * 1000
                connection.requestMethod = "POST"
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val inStream = connection.inputStream
                    val bytes = read(inStream)
                    val jsonStr = String(bytes)
                    val json = Gson().fromJson(jsonStr, Response::class.java)
                    return json?.data
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

        fun download(url: String, fileName: String, progress: ((Float) -> Unit), complete: ((File) -> Unit), error: ((File) -> Unit)) {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.connectTimeout = 3 * 60 * 1000
            connection.readTimeout = 3 * 60 * 1000
            connection.requestMethod = "POST"
            val file = File(pgyer.context!!.cacheDir, fileName)
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
        }
    }
}

