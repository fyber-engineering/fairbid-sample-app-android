/*
 * Copyright (c) 2019. Fyber N.V
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fyber.fairbid.utilities

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
import com.fyber.fairbid.sample.R

/**
 * Internal class for displaying the sample with multiple choices.
 * This fragment does not contain any sample code for FairBid
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
        initializeRecyclerView(view)
        initVersionTextView(view)
        return view
    }

    private fun initializeRecyclerView(view: View) {
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


    class SeparatorViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_separator_row, parent, false))

    enum class RowType() { Row, Separator }
    enum class UnitType() { Interstitial, Rewarded, Banner, TestSuite }
    data class UnitRowData(val unitText: String, val resourceImage: Int, val unitType: UnitType)
    data class Row(val type: RowType = RowType.Row, val payload: Any? = null)

    private val mUnits = listOf(
        Row(
            payload = UnitRowData(
                "Banner",
                R.drawable.banner_icon,
                UnitType.Banner
            )
        ),
        Row(
            payload = UnitRowData(
                "Interstitial",
                R.drawable.interstitial_icon,
                UnitType.Interstitial
            )
        ),
        Row(
            payload = UnitRowData(
                "Rewarded",
                R.drawable.rewarded_icon,
                UnitType.Rewarded
            )
        ),
        Row(type = RowType.Separator),
        Row(
            payload = UnitRowData(
                "Test Suite",
                R.drawable.ic_test_suite,
                UnitType.TestSuite
            )
        )
    )

    class ListAdapter(private val list: List<Row>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private var mContext: Context? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            this.mContext = parent.context
            val inflater = LayoutInflater.from(parent.context)
            return if (viewType == RowType.Row.ordinal) {
                UnitRowDataHolder(inflater, parent)
            } else {
                SeparatorViewHolder(inflater, parent)
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