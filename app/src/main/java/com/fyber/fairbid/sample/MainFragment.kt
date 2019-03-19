package com.fyber.fairbid.sample

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.fyber.FairBid


class MainFragment : Fragment() {

    interface FragmentListener {
        fun onButtonClicked(id: Int)
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

    private fun initVersionTextView(view:View)
    {
        fairBidVersionTextView = view.findViewById(R.id.fairbid_version)
        fairBidVersionTextView.text = String.format("%s %s",fairBidVersionTextView.text ,FairBid.SDK_VERSION )
    }

    class UnitRowDataHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_unit_row, parent, false)) {

        private var unitIcon: ImageView = itemView.findViewById(R.id.row_unit_image)
        private var unitText: TextView = itemView.findViewById(R.id.row_text_unit)
        private var rightArrow: ImageView = itemView.findViewById(R.id.right_unit_arrow)
        private var seperatorLine : View = itemView.findViewById(R.id.row_border)

        fun bind(unitRowData: UnitRowData, context: Context?) {
            unitIcon.background = context!!.getDrawable(unitRowData.resourceImage)
            unitText.text = unitRowData.unitText
            rightArrow.setOnClickListener {
                (context as FragmentListener).onButtonClicked(unitRowData.resourceImage)

            }
            seperatorLine.visibility = View.VISIBLE
        }
    }

    data class UnitRowData(val unitText: String, val resourceImage: Int)

    private val mUnits = listOf(
        UnitRowData("Interstitial", R.drawable.interstitial_icon),
        UnitRowData("Rewarded", R.drawable.rewarded_icon),
        UnitRowData("Banner", R.drawable.banner_icon)
        //UnitRowData("Test Suite", R.drawable.ic_test_suite)
    )

    class ListAdapter(private val list: List<UnitRowData>) : RecyclerView.Adapter<UnitRowDataHolder>() {
        private var mContext: Context? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnitRowDataHolder {
            val inflater = LayoutInflater.from(parent.context)
            this.mContext = parent.context
            return UnitRowDataHolder(inflater, parent)
        }

        override fun onBindViewHolder(holder: UnitRowDataHolder, position: Int) {
            val unitRowData: UnitRowData = list[position]
            holder.bind(unitRowData, mContext)
        }

        override fun getItemCount(): Int = list.size

    }

}