/*
 * Copyright (c) 2020. Fyber N.V
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
import android.support.v4.content.ContextCompat
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
import com.fyber.fairbid.ads.ImpressionData
import com.fyber.fairbid.ads.banner.BannerError
import com.fyber.fairbid.ads.banner.BannerListener
import com.fyber.fairbid.ads.banner.BannerOptions
import com.fyber.fairbid.utilities.OnScreenCallbacksHelper
import com.fyber.fairbid.utilities.MainFragment

/**
 * Log tag
 */
private const val BANNER_FRAGMENT_TAG = "BannerFragment"

/**
 * A Fragment demonstrating how to request and display banner ads using the FairBid SDK.
 */
class BannerFragment : Fragment(), OnScreenCallbacksHelper.LogsListener {

    companion object {
        /**
         * The Banner's placement name - as configured at Fyber console
         * "BannerPlacementIdExample" can be used using the provided example APP_ID
         * TODO change to your own configured placement.
         */
        private const val BANNER_PLACEMENT_NAME = "197407"
    }

    private lateinit var loadBannerButton: View
    private lateinit var cleanCallBacks: Button
    private lateinit var destroyBannerButton: Button
    private lateinit var bannerContainer: ViewGroup
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private var fragmentView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (fragmentView == null) {
            fragmentView = inflater.inflate(R.layout.ad_container_fragment, container, false)
            fragmentView?.let { it ->
                initializeUiElements(it)
                setListener()
            }
        }
        return fragmentView
    }

    /**
     * Called when the loadBannerButton is clicked
     * This function provides an example for calling the API method Banner.display in order to display a banner placement
     * @param bannerPlacementName name of placement to be requested
     */
    private fun displayBanner(bannerPlacementName: String) {
        Log.v(BANNER_FRAGMENT_TAG, "displayBanner()")
        val bannerOptions: BannerOptions = generateBannerOptions()
        Banner.show(bannerPlacementName, bannerOptions, activity)
        startRequestAnimation()
    }

    /**
     * Convenience method. Generates a new instance of BannerOptions and configure it accordingly.
     */
    private fun generateBannerOptions(): BannerOptions {
        //Calling the API method BannerOptions().placeInContainer in order to set banner position in the desired view group
        return BannerOptions().placeInContainer(bannerContainer)
    }

    /**
     * Called when the destroyBannerButton is clicked
     * This function provides an example for calling the API method Banner.destroy in order to destroy a banner placement
     * @param bannerPlacementName name of placement to be destroyed
     */
    private fun destroyBanner(bannerPlacementName: String) {
        Log.v(BANNER_FRAGMENT_TAG, "destroyBanner()")
        Banner.destroy(bannerPlacementName)
        resetAnimation()
    }

    /**
     * This function provides an example of Listening to FairBid Banner Callbacks and events.
     */
    private fun setListener() {
        val bannerListener = object : BannerListener {
            override fun onShow(placement: String, impressionData: ImpressionData) {
                Log.v(BANNER_FRAGMENT_TAG, "onShow $placement")
                OnScreenCallbacksHelper.logAndToast(recyclerView, context, OnScreenCallbacksHelper.ON_SHOW)
            }

            override fun onRequestStart(placement: String) {
                Log.v(BANNER_FRAGMENT_TAG, "onRequestStart $placement")
                OnScreenCallbacksHelper.logAndToast(recyclerView, context, OnScreenCallbacksHelper.ON_REQUEST_START)
            }

            override fun onClick(placement: String) {
                Log.v(BANNER_FRAGMENT_TAG, "onClick $placement")
                OnScreenCallbacksHelper.logAndToast(recyclerView, context, OnScreenCallbacksHelper.ON_CLICK)
            }

            override fun onLoad(placement: String) {
                Log.v(BANNER_FRAGMENT_TAG, "onLoad $placement")
                OnScreenCallbacksHelper.logAndToast(recyclerView, context, OnScreenCallbacksHelper.ON_LOAD)
                onAdAvailableAnimation()
            }

            override fun onError(placement: String, error: BannerError) {
                Log.v(BANNER_FRAGMENT_TAG, "onError $placement, error:" + error.errorMessage)
                OnScreenCallbacksHelper.logAndToast(recyclerView, context, OnScreenCallbacksHelper.ON_ERROR + ": " + error.errorMessage)
                resetAnimation()
            }

        }
        Banner.setBannerListener(bannerListener)
    }

    /**
     * Internal sample method. initialize the UI elements in this fragment.
     * @param view the container view for this fragment
     */
    private fun initializeUiElements(view: View) {
        initLogRecycler(view)
        initTextViews(view)
        initButtons(view)
        bannerContainer = view.findViewById(R.id.banner_container)
        bannerContainer.visibility = View.VISIBLE
    }

    /**
     * Internal sample method. initialize the recycler view used to display callbacks and events.
     * @param view the container view for this fragment
     */
    private fun initLogRecycler(view: View) {
        recyclerView = view.findViewById(R.id.recycler_callbacks)
        OnScreenCallbacksHelper.configureRecycler(recyclerView, activity!!, this)
    }

    /**
     * Internal sample method. initialize the text views in this fragment
     * @param view the container view for this fragment
     */
    private fun initTextViews(view: View) {
        val placementName: TextView = view.findViewById(R.id.placement_name_tv) as TextView
        placementName.text = BANNER_PLACEMENT_NAME
        val headerName: TextView = view.findViewById(R.id.fragment_header) as TextView
        headerName.text = getString(R.string.banner_header_name)
        val placementIcon: ImageView = view.findViewById(R.id.placement_icon) as ImageView
        placementIcon.background = ContextCompat.getDrawable(context!!, R.drawable.banner_icon)
    }

    /**
     * Internal sample method. initialize the buttons and click listeners in this fragment
     * @param view the container view for this fragment
     */
    private fun initButtons(view: View) {
        progressBar = view.findViewById(R.id.progress_bar)
        val textView: TextView = view.findViewById(R.id.request_ad)
        textView.text = getString(R.string.show)
        loadBannerButton = view.findViewById(R.id.text_progress_bar)

        loadBannerButton.setOnClickListener {
            displayBanner(BANNER_PLACEMENT_NAME)
        }
        destroyBannerButton = view.findViewById(R.id.show_ad)
        destroyBannerButton.background  = ContextCompat.getDrawable(context!!, R.drawable.button_effect_banner)
        destroyBannerButton.text = getString(R.string.destroy)
        destroyBannerButton.setOnClickListener {
            destroyBanner(BANNER_PLACEMENT_NAME)
        }
        val backButton: ImageView = view.findViewById(R.id.back_button) as ImageView
        backButton.setOnClickListener {
            activity!!.onBackPressed()
        }

        cleanCallBacks = view.findViewById(R.id.clean_callback_button) as Button
        cleanCallBacks.setOnClickListener {
            cleanCallBacks.isEnabled = false
            OnScreenCallbacksHelper.clearLog(recyclerView)
        }
    }

    /**
     * Internal sample method.
     * Starts the request/loading animation
     */
    private fun startRequestAnimation() {
        progressBar.visibility = View.VISIBLE
        destroyBannerButton.isEnabled = false
    }

    /**
     * Internal sample method.
     * Stops the request/loading animation and enables destroying the banner
     */
    private fun onAdAvailableAnimation() {
        destroyBannerButton.isEnabled = true
        progressBar.visibility = View.GONE
    }

    /**
     * Internal sample method.
     * Resets the UI state for the progress animation / destroy button
     */
    private fun resetAnimation() {
        destroyBannerButton.isEnabled = false
        progressBar.visibility = View.GONE
    }

    /**
     * Invoked when the on-screen log became non-empty. used to enable/disable the clean button
     */
    override fun onFirstLogLine() {
        cleanCallBacks.isEnabled = true
    }
}