package com.iwdael.install.widget

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.net.Uri
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.iwdael.coolprogress.LineProgressBar
import com.iwdael.install.Pgyer
import com.iwdael.install.compat.setScrollBottomListener
import com.iwdael.install.debug.R
import com.iwdael.install.http.HttpClient
import com.iwdael.install.http.Version
import com.iwdael.install.widget.base.BaseDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

/**
 * author : Iwdael
 * e-mail : iwdael@outlook.com
 * time   : 2019/8/5
 * desc   : MVVM
 * version: 1.0
 */
class HistoryDialog(context: Context, private val versions: List<Version>) : BaseDialog(context) {


    override fun attachLayoutRes() = R.layout.dialog_pgyer_history
    private lateinit var rcHistory: RecyclerView
    private val adapter = Adapter()
    private var hasNextPage = true
    private var currentPage = 1
    private var isLoading = false
    override fun onReady() {
        if (versions.isNotEmpty() && versions.size != 20) hasNextPage = false
        setCanceledOnTouchOutside(false)
        rcHistory = findViewById<RecyclerView>(R.id.rcHistory)
        findViewById<TextView>(R.id.tvBack)
                .setOnClickListener {
                    backListener.invoke()
                    dismiss()
                }
        rcHistory.adapter = adapter
        adapter.setOnClickListener {
            it ?: return@setOnClickListener
            val url = "https://www.pgyer.com/apiv2/app/install?_api_key=${Pgyer.pgyer._api_key}&buildKey=${it.buildKey}&buildPassword=${Pgyer.pgyer.password}"
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW).setData(uri)
            context.startActivity(intent)
        }
        rcHistory.addItemDecoration(Divider(context))
        rcHistory.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rcHistory.setScrollBottomListener {
            if (isLoading) return@setScrollBottomListener
            if (!hasNextPage) return@setScrollBottomListener
            isLoading = true
            GlobalScope.launch(Dispatchers.IO) {
                val vs = HttpClient.history(currentPage + 1)
                if (vs == null) {
                    isLoading = false
                    return@launch
                }
                GlobalScope.launch(Dispatchers.Main) {
                    adapter.insert(vs)
                    isLoading = false
                    hasNextPage = vs.size == 20
                    currentPage++
                }
            }
        }
        adapter.bind(versions)

    }

    private var backListener: (() -> Unit) = {}
    fun setOnBackListener(l: (() -> Unit)): HistoryDialog {
        backListener = l
        return this
    }

    class Holder(itemView: View, l: ((Version?) -> Unit)) : RecyclerView.ViewHolder(itemView) {
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        val tvDes = itemView.findViewById<TextView>(R.id.tvDescription)

        init {
            itemView.setOnClickListener { l.invoke(entity) }
        }

        private var entity: Version? = null
        fun bind(version: Version) {
            entity = version
            tvTitle.text = version.buildVersion
            tvDes.text = version.buildUpdateDescription
        }
    }

    class Adapter : RecyclerView.Adapter<Holder>() {
        private val dataSets = ArrayList<Version>()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_pyger_version, parent, false), clickListener)
        }

        private var clickListener: ((Version?) -> Unit) = {}
        fun setOnClickListener(l: ((Version?) -> Unit)) {
            clickListener = l
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.bind(dataSets[position])
        }

        override fun getItemCount(): Int = dataSets.size
        fun bind(data: List<Version>) {
            dataSets.addAll(data)
            notifyDataSetChanged()
        }

        fun insert(data: List<Version>) {
            val oldPosition = dataSets.size
            dataSets.addAll(data)
            notifyItemRangeInserted(oldPosition, data.size)
        }
    }

    class Divider(context: Context) : RecyclerView.ItemDecoration() {
        private val verticalPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, context.resources.displayMetrics).toInt()
        private val paddingHorizontal = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, context.resources.displayMetrics).toInt()
        private val bounds = Rect()
        private val paint = Paint()

        init {
            paint.color = Color.LTGRAY
        }

        override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            super.onDraw(c, parent, state)
            for (i in 0 until parent.childCount) {
                val child = parent.getChildAt(i)
                val index = parent.getChildAdapterPosition(child)
                if (index == state.itemCount - 1) return
                parent.getDecoratedBoundsWithMargins(child, this.bounds)
                val bottom: Int = this.bounds.bottom + child.translationY.roundToInt()
                val top: Int = bottom - 1
                c.drawRect(paddingHorizontal * 1f, top * 1f, (c.width - paddingHorizontal) * 1f, bottom * 1f, paint)
            }
        }

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            when (parent.getChildAdapterPosition(view)) {
                0 -> {
                    outRect.bottom += verticalPadding
                }
                state.itemCount - 1 -> {
                    outRect.top += verticalPadding
                }
                else -> {
                    outRect.bottom += verticalPadding
                    outRect.top += verticalPadding
                }
            }
        }

    }
}