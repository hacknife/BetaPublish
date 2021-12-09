package com.iwdael.install

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.google.gson.Gson
import com.hacknife.install.debug.R
import com.iwdael.install.http.HttpClient
import com.iwdael.install.widget.TipDialog
import com.iwdael.install.widget.UpgradeDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * author : 段泽全(hacknife)
 * e-mail : hacknife@outlook.com
 * time   : 2019/8/5
 * desc   : MVVM
 * version: 1.0
 */
class Install {

    companion object {

        fun init(context: Context, _api_key: String, appKey: String, versionName: String) {
            Pgyer.pgyer.context = context
            Pgyer.pgyer._api_key = _api_key
            Pgyer.pgyer.appKey = appKey
            Pgyer.pgyer.versionName = versionName
        }

        fun install() {
            GlobalScope.launch(Dispatchers.IO) {
                val data = HttpClient.check() ?: return@launch
                if (!data.buildHaveNewVersion) return@launch
                GlobalScope.launch(Dispatchers.Main) {
                    TipDialog(Pgyer.pgyer.context!!)
                            .setTitleRes(R.string.pyger_update)
                            .setSubTitle("(${data!!.buildVersion!!})")
                            .setContentStr(data.buildUpdateDescription!!)
                            .setCancelOnTouchOutside(false)
                            .setConfirmListener {
                                UpgradeDialog(Pgyer.pgyer.context, data.downloadURL!!, data.buildFileKey!!)
                                        .setConfirmListener {
                                            Pgyer.pgyer.context.startActivity(
                                                    Intent(Intent.ACTION_VIEW)
                                                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                                            .setDataAndType(
                                                                    FileProvider.getUriForFile(
                                                                            Pgyer.pgyer.context,
                                                                            Pgyer.pgyer.context.packageName,
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
    }
}


