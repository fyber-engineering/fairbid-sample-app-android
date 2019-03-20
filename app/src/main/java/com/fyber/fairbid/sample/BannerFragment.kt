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

package com.fyber.fairbid.sample

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.fyber.fairbid.ads.Banner
import com.fyber.fairbid.ads.banner.BannerError
import com.fyber.fairbid.ads.banner.BannerListener
import com.fyber.fairbid.ads.banner.BannerOptions

//TODO add comment banner placement NAME
private const val BANNER_PLACEMENT_NAME = "Banner"
private const val BANNER_FRAGMENT_HEADER = "Banner"
private const val BANNER_FRAGMENT_TAG = "BannerFragment"


class BannerFragment : Fragment(), MainFragment.LogsListener {

    private lateinit var loadBannerButton: View
    private lateinit var cleanCallBacks: Button
    private lateinit var destroyBannerButton: Button
    private lateinit var bannerContainer: ViewGroup
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.ad_container_fragment, container, false)
        initializeUiElements(view)
        setListener()
        return view
    }

    private fun initializeUiElements(view: View) {
        initLogRecycler(view)
        initTextViews(view)
        initButtons(view)
        bannerContainer = view.findViewById(R.id.banner_container)
        bannerContainer.visibility = View.VISIBLE
    }

    private fun initLogRecycler(view: View) {
        recyclerView = view.findViewById(R.id.recycler_callbacks)
        LogsHelper.configureRecycler(recyclerView, activity!!,this)
    }

    private fun initTextViews(view: View) {
        val placementName: TextView = view.findViewById(R.id.placement_name_tv) as TextView
        placementName.text = BANNER_PLACEMENT_NAME
        val headerName: TextView = view.findViewById(R.id.fragment_header) as TextView
        headerName.text = BANNER_FRAGMENT_HEADER
        val placementIcon: ImageView = view.findViewById(R.id.placement_icon) as ImageView
        placementIcon.background = context!!.getDrawable(R.drawable.banner_icon)
    }


    private fun initButtons(view: View) {
        progressBar = view.findViewById(R.id.progress_bar)
        val textView: TextView = view.findViewById(R.id.request_ad)
        textView.text = getString(R.string.show)
        loadBannerButton = view.findViewById(R.id.text_progress_bar)

        loadBannerButton.setOnClickListener {
            displayBanner()
        }
        destroyBannerButton = view.findViewById(R.id.show_ad)
        destroyBannerButton.text = getString(R.string.destroy)
        destroyBannerButton.setOnClickListener {
            destroyBanner()
        }
        val backButton: ImageView = view.findViewById(R.id.back_button) as ImageView
        backButton.setOnClickListener {
            activity!!.onBackPressed()
        }

        cleanCallBacks = view.findViewById(R.id.clean_callback_button) as Button
        cleanCallBacks.setOnClickListener {
            cleanCallBacks.background = context!!.getDrawable(R.drawable.clean_callback_button_disabled)
            cleanCallBacks.isEnabled = false
            LogsHelper.clearLog(recyclerView)
        }
    }


    private fun displayBanner() {
        //TODO added comment display banner
        Log.v(BANNER_FRAGMENT_TAG, "displayBanner()")
        val bannerOptions: BannerOptions = getBannerOptions()
        Banner.display(BANNER_PLACEMENT_NAME, bannerOptions, activity)
        startRequestAnimiaon()
    }

    //TODO add comment this function is OPTIONAL, default is bottom
    private fun getBannerOptions(): BannerOptions {
        return BannerOptions().placeInContainer(bannerContainer)
    }

    private fun destroyBanner() {
        //TODO added comment destroy banner
        Log.v(BANNER_FRAGMENT_TAG, "destroyBanner()")
        Banner.destroy(BANNER_PLACEMENT_NAME)
        resetAnimation()
    }

    private fun setListener() {
        //TODO add comment LISTENER
        val bannerListener = object : BannerListener {
            override fun onClick(placement: String) {
                Log.v(BANNER_FRAGMENT_TAG, "onClick $placement")
                LogsHelper.logAndToast(recyclerView, context, LogsHelper.ON_CLICK)
            }

            override fun onLoad(placement: String) {
                Log.v(BANNER_FRAGMENT_TAG, "onLoad $placement")
                LogsHelper.logAndToast(recyclerView, context, LogsHelper.ON_LOAD)
                onAdAvilabileAnimation()
            }

            override fun onError(placement: String, error: BannerError) {
                Log.v(BANNER_FRAGMENT_TAG, "onerror $placement , error :" + error.errorMessage)
                LogsHelper.logAndToast(recyclerView, context, LogsHelper.ON_ERROR)
                resetAnimation()
            }

            override fun onShow(placement: String) {
                Log.v(BANNER_FRAGMENT_TAG, "onShow $placement")
                LogsHelper.logAndToast(recyclerView, context, LogsHelper.ON_SHOW)
            }
        }
        Banner.setBannerListener(bannerListener)
    }

    private fun startRequestAnimiaon() {
        progressBar.visibility = View.VISIBLE
        destroyBannerButton.background = context!!.getDrawable(R.drawable.button_disabled)
    }

    private fun onAdAvilabileAnimation() {
        progressBar.visibility = View.GONE
        destroyBannerButton.background = context!!.getDrawable(R.drawable.button_enabled)
    }

    private fun resetAnimation() {
        progressBar.visibility = View.GONE
        destroyBannerButton.background = context!!.getDrawable(R.drawable.button_disabled)
    }

    override fun onFirstLogLine() {
        cleanCallBacks.background = context!!.getDrawable(R.drawable.clean_callback_button_enabled)
    }
}