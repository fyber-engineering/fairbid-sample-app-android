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
import androidx.core.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.fyber.fairbid.ads.ImpressionData
import com.fyber.fairbid.ads.Interstitial
import com.fyber.fairbid.ads.interstitial.InterstitialListener
import com.fyber.fairbid.utilities.OnScreenCallbacksHelper
import com.fyber.fairbid.utilities.MainFragment

/**
 * Log tag
 */
private const val INTERSTITIAL_FRAGMENT_TAG = "InterstitialFragment"

/**
 * Display interstitial ads
 */
class InterstitialFragment : Fragment(), OnScreenCallbacksHelper.LogsListener {


    companion object {
        /**
         * The Interstitial's placement name - as configured at Fyber console
         * "InterstitialPlacementIdExample" can be used using the provided example APP_ID
         * TODO change to your own configured placement.
         */
        private const val INTERSTITIAL_PLACEMENT_NAME = "197405"
    }

    private lateinit var cleanCallBacks: Button
    private lateinit var requestButton: View
    private lateinit var showButton: Button
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
     * Called when the requestButton is clicked
     * This function provides an example for calling the API method Interstitial.request in order to request an interstitial ad
     * @param interstitialPlacementName name of placement to be requested
     */
    private fun requestInterstitial(interstitialPlacementName: String) {
        Log.v(INTERSTITIAL_FRAGMENT_TAG, "Requesting Interstitial")
        /** request a new ad in case there is no available ad to show */
        if (!Interstitial.isAvailable(interstitialPlacementName)) {
            Interstitial.request(interstitialPlacementName)
            startRequestAnimation()
        }
    }

    /**
     * Called when the showButton is clicked
     * This function provides an example for calling the API method Interstitial.show in order to show the returned ad
     * @param interstitialPlacementName name of placement to be displayed
     */
    private fun showInterstitial(interstitialPlacementName: String) {
        Log.v(INTERSTITIAL_FRAGMENT_TAG, "Requesting Interstitial")
        Interstitial.show(interstitialPlacementName, activity)
        resetAnimation()
    }

    /**
     * This function provides an example of Listening to FairBid Interstitial Callbacks and events.
     */
    private fun setListener() {
        val interstitialListener = object : InterstitialListener {
            override fun onShow(placement: String, impressionData: ImpressionData) {
                Log.v(INTERSTITIAL_FRAGMENT_TAG, "onShow $placement")
                OnScreenCallbacksHelper.logAndToast(recyclerView, context, OnScreenCallbacksHelper.ON_SHOW)
            }

            override fun onShowFailure(placement: String, impressionData: ImpressionData) {
                Log.v(INTERSTITIAL_FRAGMENT_TAG, "onShowFailure $placement")
                OnScreenCallbacksHelper.logAndToast(recyclerView, context, OnScreenCallbacksHelper.ON_SHOW_FAILURE)
            }

            override fun onRequestStart(placement: String) {
                Log.v(INTERSTITIAL_FRAGMENT_TAG, "onShowFailure $placement")
                OnScreenCallbacksHelper.logAndToast(recyclerView, context, OnScreenCallbacksHelper.ON_REQUEST_START)
            }


            override fun onClick(placement: String) {
                Log.v(INTERSTITIAL_FRAGMENT_TAG, "onClick $placement")
                OnScreenCallbacksHelper.logAndToast(recyclerView, context, OnScreenCallbacksHelper.ON_CLICK)
            }

            override fun onHide(placement: String) {
                Log.v(INTERSTITIAL_FRAGMENT_TAG, "onHide $placement")
                OnScreenCallbacksHelper.logAndToast(recyclerView, context, OnScreenCallbacksHelper.ON_HIDE)

            }


            override fun onAvailable(placement: String) {
                Log.v(INTERSTITIAL_FRAGMENT_TAG, "onAvailable $placement")
                OnScreenCallbacksHelper.logAndToast(recyclerView, context, OnScreenCallbacksHelper.ON_AVAILABLE)
                onAdAvailableAnimation()
            }

            override fun onUnavailable(placement: String) {
                Log.v(INTERSTITIAL_FRAGMENT_TAG, "onUnavailable $placement")
                OnScreenCallbacksHelper.logAndToast(recyclerView, context, OnScreenCallbacksHelper.ON_UNAVAILABLE)
                resetAnimation()
            }

        }
        Interstitial.setInterstitialListener(interstitialListener)
    }

    /**
     * Internal sample method. initialize the UI elements in this fragment.
     * @param view the container view for this fragment
     */
    private fun initializeUiElements(view: View) {
        initLogRecycler(view)
        initTextViews(view)
        initButtons(view)
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
     * Internal sample method. initialize the buttons and click listeners in this fragment
     * @param view the container view for this fragment
     */
    private fun initButtons(view: View) {
        requestButton = view.findViewById(R.id.text_progress_bar)
        requestButton.setOnClickListener {
            requestInterstitial(INTERSTITIAL_PLACEMENT_NAME)
        }
        showButton = view.findViewById(R.id.show_ad)
        showButton.setOnClickListener {
            showInterstitial(INTERSTITIAL_PLACEMENT_NAME)
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
        progressBar = view.findViewById(R.id.progress_bar)
    }

    /**
     * Internal sample method. initialize the text views in this fragment
     * @param view the container view for this fragment
     */
    private fun initTextViews(view: View) {
        val placementName: TextView = view.findViewById(R.id.placement_name_tv) as TextView
        placementName.text = INTERSTITIAL_PLACEMENT_NAME
        val headerName: TextView = view.findViewById(R.id.fragment_header) as TextView
        headerName.text = getString(R.string.interstitial_header_name)
        val placementIcon: ImageView = view.findViewById(R.id.placement_icon) as ImageView
        placementIcon.background = ContextCompat.getDrawable(context!!, R.drawable.interstitial_icon)
    }

    /**
     * Internal sample method.
     * Starts the request/loading animation
     */
    private fun startRequestAnimation() {
        showButton.isEnabled = false
        progressBar.visibility = View.VISIBLE
    }

    /**
     * Internal sample method.
     * Stops the request/loading animation and enables destroying the banner
     */
    private fun onAdAvailableAnimation() {
        progressBar.visibility = View.GONE
        showButton.isEnabled = true
    }

    /**
     * Internal sample method.
     * Resets the UI state for the progress animation / destroy button
     */
    private fun resetAnimation() {
        showButton.isEnabled = false
        progressBar.visibility = View.GONE
    }

    /**
     * Invoked when the on-screen log became non-empty. used to enable/disable the clean button
     */
    override fun onFirstLogLine() {
        cleanCallBacks.isEnabled = true
    }
}
