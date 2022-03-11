package com.iwdael.install.widget

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * author : Iwdael
 * e-mail : iwdael@outlook.com
 * time   : 2019/8/5
 * desc   : MVVM
 * version: 1.0
 */
class RecyclerViewScrollListener(val scrollBottomListener: (() -> Unit)) : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val manager = recyclerView.layoutManager as LinearLayoutManager
        val lastItem = manager.findLastVisibleItemPosition()
        if (lastItem >= manager.itemCount - 4) {
            scrollBottomListener.invoke()
        }
    }
}