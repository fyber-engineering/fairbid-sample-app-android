package com.fyber.fairbid.sample

import android.app.Activity
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class LogsHelper {
    companion object {
        //Callbacks
        const val ON_SHOW = "onShow()"
        const val ON_CLICK = "onClick()"
        const val ON_HIDE = "onHide()"
        const val ON_SHOW_FAILURE = "onShowFailure()"
        const val ON_AVALIABLE = "onAvailable()"
        const val ON_UNAVAILABLE = "onUnavailable()"
        const val ON_AUDTIO_START = "onAudioStart()"
        const val ON_AUDIO_FINISH = "onAudioFinish()"
        const val ON_COMPLETION = "onCompletion()"
        const val ON_ERROR = "onError()"
        const val ON_LOAD = "onLoad()"

        fun configureRecycler(recyclerView: RecyclerView, activity: Activity, listener: MainFragment.LogsListener) {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(activity)
                val emptyLogRow = ArrayList<String>()
                adapter = LogsAdapter(emptyLogRow)
                (adapter as LogsAdapter).setListener(listener)
            }
        }

        fun logAndToast(recyclerView: RecyclerView, context: Context?, log: String) {
            if(context != null) {
                Toast.makeText(context, log, Toast.LENGTH_SHORT).show()
            }
            val recyclerViewAdapter: LogsAdapter = recyclerView.adapter as LogsAdapter
            recyclerViewAdapter.addLog(log)
        }

        fun clearLog(recyclerView: RecyclerView) {
            val recyclerViewAdapter: LogsAdapter = recyclerView.adapter as LogsAdapter
            recyclerViewAdapter.clearList()
        }
    }

    class LogDataHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_log_row, parent, false)) {

        private var logTextView: TextView = itemView.findViewById(R.id.row_log)

        fun bind(text: String) {
            logTextView.text = text
        }
    }

    class LogsAdapter(private val logsList: ArrayList<String>) : RecyclerView.Adapter<LogDataHolder>() {
        private lateinit var listener: MainFragment.LogsListener

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogDataHolder {
            val inflater = LayoutInflater.from(parent.context)
            return LogsHelper.LogDataHolder(inflater, parent)
        }

        override fun onBindViewHolder(holder: LogDataHolder, position: Int) {
            holder.bind(logsList[position])
        }

        override fun getItemCount(): Int = logsList.size


        fun setListener(listener: MainFragment.LogsListener) {
            this.listener = listener
        }

        fun addLog(log: String) {
            if (logsList.isEmpty()) {
                listener.onFirstLogLine()
            }
            logsList.add("${getCurrentTime()} - $log")
            notifyDataSetChanged()
        }

        fun clearList() {
            logsList.clear()
            notifyDataSetChanged()
        }

        fun getCurrentTime(): String {
            var answer: String
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
                answer = current.format(formatter)
            } else {
                var date = Date();
                val formatter = SimpleDateFormat("HH:mm:ss")
                answer = formatter.format(date)
            }
            return answer
        }
    }
}
