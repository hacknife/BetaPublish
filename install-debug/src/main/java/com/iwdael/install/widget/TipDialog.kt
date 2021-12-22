package com.iwdael.install.widget

import android.content.Context
import android.view.View
import android.widget.TextView
import com.iwdael.install.debug.R
import com.iwdael.install.widget.base.BaseDialog


/**
 * author : Iwdael
 * e-mail : iwdael@outlook.com
 * time   : 2019/8/5
 * desc   : MVVM
 * version: 1.0
 */
open class TipDialog(context: Context) : BaseDialog(context) {

    companion object {
        var dialog: TipDialog? = null
    }

    protected var confirmListener: (() -> Unit) = {}
    fun setConfirmListener(l: (() -> Unit)): TipDialog {
        confirmListener = l
        return this
    }

    private var cancelListener: (() -> Unit) = {}
    fun setCancelListener(l: (() -> Unit)): TipDialog {
        cancelListener = l
        return this
    }

    private var cancelEnable = true
    private var confirmEnable = true

    fun setCancelEnable(b: Boolean): TipDialog {
        cancelEnable = b
        return this
    }

    fun setConfirmEnable(b: Boolean): TipDialog {
        confirmEnable = b;
        return this
    }


    private var cancelRes: Int? = null
    private var confirmRes: Int? = null


    fun setCancelRes(b: Int): TipDialog {
        cancelRes = b
        return this
    }

    fun setConfirmRes(b: Int): TipDialog {
        confirmRes = b;
        if (isShowing) {
            confirmRes?.let {
                findViewById<TextView>(R.id.tvConfirm).setText(it)
            }
        }
        return this
    }


    private var titleRes: Int? = null
    private var subTitle: String? = null
    private var contentRes: Int? = null
    private var contentStr: String? = null


    fun setTitleRes(b: Int): TipDialog {
        titleRes = b
        return this
    }

    fun setSubTitle(b: String): TipDialog {
        subTitle = b
        return this
    }


    fun setContentRes(b: Int): TipDialog {
        contentRes = b;
        if (isShowing) {
            contentRes?.let {
                findViewById<TextView>(R.id.tvContent).setText(it)
            }
        }
        return this
    }

    fun setContentStr(b: String): TipDialog {
        contentStr = b;
        return this
    }

    private var cancelOnTouchOutside = true
    fun setCancelOnTouchOutside(b: Boolean): TipDialog {
        cancelOnTouchOutside = b
        return this
    }

    override fun attachLayoutRes() = R.layout.dialog_pgyer_tip

    override fun destroy() {
        dialog = null
    }

    override fun show() {

        if (dialog == null) {
            super.show()
            dialog = this
        } else {
            dialog!!.cancelOnTouchOutside = cancelOnTouchOutside

            dialog!!.confirmListener = confirmListener
            dialog!!.cancelListener = cancelListener

            dialog!!.confirmEnable = confirmEnable
            dialog!!.cancelEnable = cancelEnable
            dialog!!.confirmRes = confirmRes
            dialog!!.cancelRes = cancelRes

            dialog!!.titleRes = titleRes
            dialog!!.contentRes = contentRes
            dialog!!.contentStr = contentStr
            dialog!!.onReady()
        }
    }

    override fun onReady() {
        setCanceledOnTouchOutside(cancelOnTouchOutside)
        setOnKeyListener { dialog, keyCode, event -> true }
        findViewById<View>(R.id.tvCancel).setOnClickListener {
            cancelListener.invoke()
            dismiss()
        }
        findViewById<View>(R.id.tvConfirm).setOnClickListener {
            confirmListener.invoke()
            dismiss()
        }
        findViewById<View>(R.id.tvConfirm).visibility =
                if (confirmEnable) View.VISIBLE else View.GONE
        findViewById<View>(R.id.tvCancel).visibility = if (cancelEnable) View.VISIBLE else View.GONE
        cancelRes?.let {
            findViewById<TextView>(R.id.tvCancel).setText(it)
        }
        confirmRes?.let {
            findViewById<TextView>(R.id.tvConfirm).setText(it)
        }
        titleRes?.let {
            findViewById<TextView>(R.id.tvTitle).setText(it)
        }
        subTitle?.let {
            findViewById<TextView>(R.id.tvSubTitle).text = it
        }
        contentRes?.let {
            findViewById<TextView>(R.id.tvContent).setText(it)
        }
        contentStr?.let {
            findViewById<TextView>(R.id.tvContent).setText(it)
        }
    }
}