package com.fyber.fairbid.sample

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.fyber.FairBid

/**
 * Internal class for displaying the sample with multiple choices.
 * This fragment do not contain any sample code for FairBid
 */
class MainFragment : Fragment() {

    interface FragmentListener {
        fun onButtonClicked(unitType: UnitType)
    }
    interface LogsListener {
        fun onFirstLogLine()
    }


    private lateinit var recyclerView: RecyclerView
    private lateinit var fairBidVersionTextView: TextView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.main_fragment, container, false)
        initializeRecylerView(view)
        initVersionTextView(view)
        return view
    }

    private fun initializeRecylerView(view: View) {
        recyclerView = view.findViewById(R.id.units_recycler)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = ListAdapter(mUnits)
        }
    }

    private fun initVersionTextView(view: View) {
        fairBidVersionTextView = view.findViewById(R.id.fairbid_version)
        fairBidVersionTextView.text = String.format("%s %s", fairBidVersionTextView.text, FairBid.SDK_VERSION)
    }

    class UnitRowDataHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_unit_row, parent, false)) {

        private var unitIcon: ImageView = itemView.findViewById(R.id.row_unit_image)
        private var unitText: TextView = itemView.findViewById(R.id.row_text_unit)
        private var rightArrow: ImageView = itemView.findViewById(R.id.right_unit_arrow)

        fun bind(unitRowData: UnitRowData, context: Context?) {
            unitIcon.background = context!!.getDrawable(unitRowData.resourceImage)
            unitText.text = unitRowData.unitText
            rightArrow.setOnClickListener {
                (context as FragmentListener).onButtonClicked(unitRowData.unitType)
            }
        }
    }


    class SeperatorViewHodler(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_seperator_row, parent, false))

    enum class RowType() { Row, Seperator }
    enum class UnitType() { Interstitial, Rewarded, Banner, TestSuite }
    data class UnitRowData(val unitText: String, val resourceImage: Int, val unitType: UnitType)
    data class Row(val type: RowType = RowType.Row, val payload: Any? = null)

    private val mUnits = listOf(
        Row(payload = UnitRowData("Banner", R.drawable.banner_icon, UnitType.Banner)),
        Row(payload = UnitRowData("Interstitial", R.drawable.interstitial_icon, UnitType.Interstitial)),
        Row(payload = UnitRowData("Rewarded", R.drawable.rewarded_icon, UnitType.Rewarded)),
        Row(type = RowType.Seperator),
        Row(payload = UnitRowData("Test Suite", R.drawable.ic_test_suite, UnitType.TestSuite))
    )

    class ListAdapter(private val list: List<Row>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private var mContext: Context? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            this.mContext = parent.context
            val inflater = LayoutInflater.from(parent.context)
            return if (viewType == RowType.Row.ordinal) {
                UnitRowDataHolder(inflater, parent)
            } else {
                SeperatorViewHodler(inflater, parent)
            }
        }

        override fun getItemViewType(position: Int): Int {
            return list[position].type.ordinal
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            if (getItemViewType(position) == RowType.Row.ordinal) {

                val unitRowData: UnitRowData = list[position].payload as UnitRowData
                (holder as UnitRowDataHolder).bind(unitRowData, mContext)
                holder.itemView.setOnClickListener {
                    (mContext as FragmentListener).onButtonClicked((list[position].payload as UnitRowData).unitType)
                }
            }
        }

        override fun getItemCount(): Int = list.size
    }

}