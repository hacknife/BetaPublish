package com.hacknife.install.widget.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.Toast
import com.hacknife.install.debug.R

abstract class BaseDialog (context: Context) : Dialog(
    context,
    R.style.install_dialog
) {



    override fun onCreate(savedInstanceState: Bundle?) {

        setContentView(attachLayoutRes())
        super.onCreate(savedInstanceState)
        onReady()
        setOnDismissListener {
            destroy()
        }
    }

    open fun destroy(){}
    override fun show() {
        super.show()
        window!!.setBackgroundDrawableResource(R.drawable.base_dialog_bg)
        window!!.setGravity(Gravity.CENTER)
        window!!.decorView.setPadding(0, 0, 0, 0)
        window!!.attributes = window!!.attributes.let {
            it.width = WindowManager.LayoutParams.MATCH_PARENT
            it.height = WindowManager.LayoutParams.WRAP_CONTENT
            it
        }
    }

    abstract fun attachLayoutRes(): Int

    abstract fun onReady()

    open fun display(): BaseDialog {
        show()
        return this
    }
    fun toast(res:Int){
        Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
    }
}