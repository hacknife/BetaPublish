package com.iwdael.install

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.google.gson.Gson
import com.iwdael.install.debug.R
import com.iwdael.install.http.HttpClient
import com.iwdael.install.widget.HistoryDialog
import com.iwdael.install.widget.TipDialog
import com.iwdael.install.widget.UpgradeDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * author : Iwdael
 * e-mail : iwdael@outlook.com
 * time   : 2019/8/5
 * desc   : MVVM
 * version: 1.0
 */
class Install {

    companion object {

        fun init(_api_key: String, appKey: String, versionName: String, password: String) {
            Pgyer.pgyer._api_key = _api_key
            Pgyer.pgyer.appKey = appKey
            Pgyer.pgyer.versionName = versionName
            Pgyer.pgyer.password = password
        }

        fun install(context: Context) {
            GlobalScope.launch(Dispatchers.IO) {
                val data = HttpClient.check() ?: return@launch
                if (!data.buildHaveNewVersion) return@launch
                GlobalScope.launch(Dispatchers.Main) {
                    TipDialog(context)
                            .setTitleRes(R.string.pyger_update)
                            .setSubTitle("(${data.buildVersion!!})")
                            .setContentStr(data.buildUpdateDescription!!)
                            .setCancelOnTouchOutside(false)
                            .setHistoryListener {
                                HistoryDialog(context, it)
                                        .setOnBackListener {
                                            install(context)
                                        }
                                        .show()
                            }
                            .setConfirmListener {
                                UpgradeDialog(context, data.downloadURL!!, data.buildFileKey!!)
                                        .setConfirmListener {
                                            context.startActivity(
                                                    Intent(Intent.ACTION_VIEW)
                                                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                                            .setDataAndType(
                                                                    FileProvider.getUriForFile(
                                                                            context,
                                                                            context.packageName,
                                                                            it
                                                                    ), "application/vnd.android.package-archive"
                                                            )
                                            )
                                        }
                                        .show()
                            }
                            .show()
                }
            }

        }

        fun history(context: Context) {
            GlobalScope.launch(Dispatchers.IO) {
                HttpClient.history(1)?.let {
                    GlobalScope.launch(Dispatchers.Main) {
                        HistoryDialog(context, it).show()
                    }
                }
            }
        }
    }
}


