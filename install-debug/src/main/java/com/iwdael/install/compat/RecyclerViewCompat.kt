package com.iwdael.install.compat

import androidx.recyclerview.widget.RecyclerView
import com.iwdael.install.widget.RecyclerViewScrollListener
/**
 * author : Iwdael
 * e-mail : iwdael@outlook.com
 * time   : 2019/8/5
 * desc   : MVVM
 * version: 1.0
 */
fun RecyclerView.setScrollBottomListener(scrollBottomListener: (() -> Unit)) {
    addOnScrollListener(RecyclerViewScrollListener(scrollBottomListener))
}