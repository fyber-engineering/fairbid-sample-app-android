/*
 * Copyright (c) 2022. Fyber N.V
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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.fyber.fairbid.ads.ImpressionData
import com.fyber.fairbid.ads.Rewarded
import com.fyber.fairbid.ads.rewarded.RewardedListener
import com.fyber.fairbid.utilities.OnScreenCallbacksHelper

/**
 * Log tag
 */
private const val REWARDED_FRAGMENT_TAG = "RewardedFragment"

/**
 * A Fragment demonstrating how to request and display interstitial ads using the FairBid SDK.
 */
class RewardedFragment : Fragment(), OnScreenCallbacksHelper.LogsListener {

    companion object {
        /**
         * The Rewarded's placement name - as configured at Fyber console
         * "RewardedPlacementIdExample" can be used using the provided example APP_ID
         * TODO change to your own configured placement.
         */
        private const val REWARDED_PLACEMENT_NAME = "205576"
    }

    private lateinit var requestButton: View
    private lateinit var cleanCallBacks: Button
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
     * This function provides an example for calling the API method Rewarded.rqueest in order to request a rewarded placement
     * @param rewardedPlacementName name of placement to be requested
     */
    private fun requestRewarded(rewardedPlacementName: String) {
        Log.v(REWARDED_FRAGMENT_TAG, "Requesting RewardedVideo")
        /** request a new ad in case there is no available ad to show */
        if (!Rewarded.isAvailable(rewardedPlacementName)) {
            Rewarded.request(rewardedPlacementName)
            startRequestAnimation()
        }
    }

    /**
     * Called when the showButton is clicked
     * This function provides an example for calling the API method Rewarded.show in order to show the ad received in the provided placement
     * @param rewardedPlacementName name of placement to be displayed
     */
    private fun showRewarded(rewardedPlacementName: String) {
        Log.v(REWARDED_FRAGMENT_TAG, "Showing RewardedVideo")
        Rewarded.show(rewardedPlacementName, activity)
        resetAnimation()
    }

    /**
     * This function provides an example of Listening to FairBid BanRewardedner Callbacks and events.
     */
    private fun setListener() {
        val rewardedListener = object : RewardedListener {
            override fun onShow(placement: String, impressionData: ImpressionData) {
                Log.v(REWARDED_FRAGMENT_TAG, "onShow $placement")
                OnScreenCallbacksHelper.logAndToast(recyclerView, context, OnScreenCallbacksHelper.ON_SHOW)
            }

            override fun onShowFailure(placement: String, impressionData: ImpressionData) {
                Log.v(REWARDED_FRAGMENT_TAG, "onShowFailure $placement")
                OnScreenCallbacksHelper.logAndToast(recyclerView, context, OnScreenCallbacksHelper.ON_SHOW_FAILURE)
            }

            override fun onRequestStart(placement: String) {
                Log.v(REWARDED_FRAGMENT_TAG, "onShowFailure $placement")
                OnScreenCallbacksHelper.logAndToast(recyclerView, context, OnScreenCallbacksHelper.ON_REQUEST_START)
            }

            override fun onClick(placement: String) {
                Log.v(REWARDED_FRAGMENT_TAG, "onClick $placement")
                OnScreenCallbacksHelper.logAndToast(recyclerView, context, OnScreenCallbacksHelper.ON_CLICK)
            }

            override fun onHide(placement: String) {
                Log.v(REWARDED_FRAGMENT_TAG, "onHide $placement")
                OnScreenCallbacksHelper.logAndToast(recyclerView, context, OnScreenCallbacksHelper.ON_HIDE)
            }

            override fun onAvailable(placement: String) {
                Log.v(REWARDED_FRAGMENT_TAG, "onAvailable $placement")
                OnScreenCallbacksHelper.logAndToast(recyclerView, context, OnScreenCallbacksHelper.ON_AVAILABLE)
                onAdAvailableAnimation()
            }

            override fun onUnavailable(placement: String) {
                Log.v(REWARDED_FRAGMENT_TAG, "onUnavailable $placement")
                OnScreenCallbacksHelper.logAndToast(recyclerView, context, OnScreenCallbacksHelper.ON_UNAVAILABLE)
                resetAnimation()
            }

            override fun onCompletion(placement: String, userRewarded: Boolean) {
                OnScreenCallbacksHelper.logAndToast(recyclerView, context, if (userRewarded) OnScreenCallbacksHelper.ON_COMPLETION else "${OnScreenCallbacksHelper.ON_COMPLETION}: $userRewarded")
                Log.v(REWARDED_FRAGMENT_TAG, "onCompletion rewarded status: $userRewarded, $placement")
            }

        }
        Rewarded.setRewardedListener(rewardedListener)
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
        OnScreenCallbacksHelper.configureRecycler(recyclerView, requireActivity(), this)
    }

    /**
     * Internal sample method. initialize the text views in this fragment
     * @param view the container view for this fragment
     */
    private fun initTextViews(view: View) {
        val placementName: TextView = view.findViewById(R.id.placement_name_tv) as TextView
        placementName.text = REWARDED_PLACEMENT_NAME
        val headerName: TextView = view.findViewById(R.id.fragment_header) as TextView
        headerName.text = getString(R.string.rewarded_header_name)
        val placementIcon: ImageView = view.findViewById(R.id.placement_icon) as ImageView
        placementIcon.background = ContextCompat.getDrawable(requireContext(), R.drawable.fb_ic_rewarded)
    }

    /**
     * Internal sample method. initialize the buttons and click listeners in this fragment
     * @param view the container view for this fragment
     */
    private fun initButtons(view: View) {
        requestButton = view.findViewById(R.id.text_progress_bar)
        requestButton.setOnClickListener {
            requestRewarded(REWARDED_PLACEMENT_NAME)
        }
        showButton = view.findViewById(R.id.show_ad)
        showButton.setOnClickListener {
            showRewarded(REWARDED_PLACEMENT_NAME)
        }
        val backButton: ImageView = view.findViewById(R.id.back_button) as ImageView
        backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        cleanCallBacks = view.findViewById(R.id.clean_callback_button) as Button
        cleanCallBacks.setOnClickListener {
            cleanCallBacks.isEnabled = false
            OnScreenCallbacksHelper.clearLog(recyclerView)
        }
        progressBar = view.findViewById(R.id.progress_bar)
    }

    /**
     * Internal sample method.
     * Starts the request/loading animation
     */
    private fun startRequestAnimation() {
        progressBar.visibility = View.VISIBLE
        showButton.isEnabled = false
    }

    /**
     * Internal sample method.
     * Stops the request/loading animation and enables destroying the banner
     */
    private fun onAdAvailableAnimation() {
        showButton.isEnabled = true
        progressBar.visibility = View.GONE
    }

    /**
     * Internal sample method.
     * Resets the UI state for the progress animation / destroy button
     */
    private fun resetAnimation() {
        progressBar.visibility = View.GONE
        showButton.isEnabled = false
    }

    /**
     * Invoked when the on-screen log became non-empty. used to enable/disable the clean button
     */
    override fun onFirstLogLine() {
        cleanCallBacks.isEnabled = true
    }


}
