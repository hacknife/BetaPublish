package com.iwdael.install.widget

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView

import com.hacknife.coolprogress.LineProgressBar
import com.hacknife.install.debug.R
import com.iwdael.install.http.HttpClient
import com.iwdael.install.widget.base.BaseDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.concurrent.TimeUnit

/**
 * author : 段泽全(hacknife)
 * e-mail : hacknife@outlook.com
 * time   : 2019/8/5
 * desc   : MVVM
 * version: 1.0
 */
class UpgradeDialog(context: Context, private val link: String, private val name: String) : BaseDialog(context) {


    lateinit var lineProgress: LineProgressBar
    lateinit var tvProgress: TextView
    private var confirmListener: ((File) -> Unit) = {}
    fun setConfirmListener(l: ((File) -> Unit)): UpgradeDialog {
        confirmListener = l
        return this
    }

    override fun attachLayoutRes() = R.layout.dialog_pgyer_upgrade


    override fun onReady() {
        lineProgress = findViewById<LineProgressBar>(R.id.lineProgress)
        tvProgress = findViewById<TextView>(R.id.tvProgress)
        setCanceledOnTouchOutside(false)
        findViewById<View>(R.id.tvCancel).setOnClickListener {
            dismiss()
        }
        findViewById<View>(R.id.tvConfirm).setOnClickListener {
            confirmListener.invoke(it.getTag(R.id.tvConfirm) as File)
            dismiss()
        }

        GlobalScope.launch(Dispatchers.IO) {
            HttpClient.download(link, name, {
                findViewById<View>(R.id.tvConfirm).isEnabled = false
                lineProgress.setCurProgress(it.toInt(), 0)
                tvProgress.text = String.format("%.2f%%", it)
            }, {
                findViewById<View>(R.id.tvConfirm).isEnabled = true
                findViewById<TextView>(R.id.tvConfirm).setTextColor(Color.parseColor("#16B99A"))
                findViewById<TextView>(R.id.tvConfirm).setTag(R.id.tvConfirm, it)
            }, {
                findViewById<View>(R.id.tvConfirm).isEnabled = false
                findViewById<TextView>(R.id.tvConfirm).visibility = View.GONE
                findViewById<TextView>(R.id.tvTitle).setText(R.string.pyger_download_fail)
            })
        }


    }
}