package com.iwdael.install.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.iwdael.install.debug.R
import kotlin.math.max

/**
 * author : Iwdael
 * e-mail : iwdael@outlook.com
 * time   : 2019/8/5
 * desc   : MVVM
 * version: 1.0
 */
class MaxRecyclerView(context: Context, attrs: AttributeSet?) : RecyclerView(context, attrs) {
    private val maxHeight: Float

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.MaxRecyclerView)
        maxHeight = ta.getDimension(R.styleable.MaxRecyclerView_android_maxHeight, 0f)
        ta.recycle()
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        if (maxHeight != 0f)
            super.onMeasure(widthSpec, MeasureSpec.makeMeasureSpec(maxHeight.toInt(), MeasureSpec.AT_MOST))
        else
            super.onMeasure(widthSpec, heightSpec)

    }
}