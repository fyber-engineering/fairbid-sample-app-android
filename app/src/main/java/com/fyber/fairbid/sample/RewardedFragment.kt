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
import com.fyber.fairbid.ads.Rewarded
import com.fyber.fairbid.ads.rewarded.RewardedListener
import com.fyber.fairbid.utilities.LogsHelper
import com.fyber.fairbid.utilities.MainFragment

private const val REWARDED_FRAGMENT_HEADER = "Rewarded"
private const val REWARDED_FRAGMENT_TAG = "RewardedFragment"

/**
 * The Rewarded Fragment,
 * Responsible to demonstrate how to display rewarded ads
 */
class RewardedFragment : Fragment(), MainFragment.LogsListener {

    companion object {
        /** Rewarded's placement name */
        private const val REWARDED_PLACEMENT_NAME = "Rewarded"
    }

    private lateinit var requestButton: View
    private lateinit var cleanCallBacks: Button
    private lateinit var showButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.ad_container_fragment, container, false)
        initializeUiElements(view)
        setListener()
        if (Rewarded.isAvailable(REWARDED_PLACEMENT_NAME)) {
            onAdAvailableAnimation()
        }
        return view
    }

    private fun initializeUiElements(view: View) {
        initLogRecycler(view)
        initTextViews(view)
        initButtons(view)
    }

    private fun initLogRecycler(view: View) {
        recyclerView = view.findViewById(R.id.recycler_callbacks)
        LogsHelper.configureRecycler(recyclerView, activity!!, this)
    }

    private fun initTextViews(view: View) {
        val placementName: TextView = view.findViewById(R.id.placement_name_tv) as TextView
        placementName.text = REWARDED_PLACEMENT_NAME
        val headerName: TextView = view.findViewById(R.id.fragment_header) as TextView
        headerName.text = REWARDED_FRAGMENT_HEADER
        val placementIcon: ImageView = view.findViewById(R.id.placement_icon) as ImageView
        placementIcon.background = context!!.getDrawable(R.drawable.rewarded_icon)
    }

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
            activity!!.onBackPressed()
        }

        cleanCallBacks = view.findViewById(R.id.clean_callback_button) as Button
        cleanCallBacks.setOnClickListener {
            cleanCallBacks.background = context!!.getDrawable(R.drawable.clean_callback_button_disabled)
            cleanCallBacks.isEnabled = false
            LogsHelper.clearLog(recyclerView)
        }
        progressBar = view.findViewById(R.id.progress_bar)
    }

    /**
     * Called when the requestButton is clicked
     * @param rewardedPlacementName name of placement to be requested
     */
    private fun requestRewarded(rewardedPlacementName: String) {
        Log.v(REWARDED_FRAGMENT_TAG, "Requesting RewardedVideo")
        /** Is interstitial Rewarded name is available */
        if (!Rewarded.isAvailable(rewardedPlacementName)) {
            Rewarded.request(rewardedPlacementName)
            startRequestAnimation()
        }
    }

    /**
     * Called when the showButton is clicked
     * @param rewardedPlacementName name of placement to be displayed
     */
    private fun showRewarded(rewardedPlacementName: String) {
        Log.v(REWARDED_FRAGMENT_TAG, "Requesting RewardedVideo")
        Rewarded.show(rewardedPlacementName, activity)
        resetAnimation()
    }

    /**
     * Listen to FairBid Rewarded Callbacks
     */
    private fun setListener() {
        val rewardedListener = object : RewardedListener {
            override fun onShow(placement: String) {
                Log.v(REWARDED_FRAGMENT_TAG, "onShow $placement")
                LogsHelper.logAndToast(recyclerView, context, LogsHelper.ON_SHOW)
            }

            override fun onClick(placement: String) {
                Log.v(REWARDED_FRAGMENT_TAG, "onClick $placement")
                LogsHelper.logAndToast(recyclerView, context, LogsHelper.ON_CLICK)
            }

            override fun onHide(placement: String) {
                Log.v(REWARDED_FRAGMENT_TAG, "onHide $placement")
                LogsHelper.logAndToast(recyclerView, context, LogsHelper.ON_HIDE)
            }

            override fun onShowFailure(placement: String) {
                Log.v(REWARDED_FRAGMENT_TAG, "onShowFailure $placement")
                LogsHelper.logAndToast(recyclerView, context, LogsHelper.ON_SHOW_FAILURE)
            }

            override fun onAvailable(placement: String) {
                Log.v(REWARDED_FRAGMENT_TAG, "onAvailable $placement")
                LogsHelper.logAndToast(recyclerView, context, LogsHelper.ON_AVALIABLE)
                onAdAvailableAnimation()
            }

            override fun onUnavailable(placement: String) {
                Log.v(REWARDED_FRAGMENT_TAG, "onUnavailable $placement")
                LogsHelper.logAndToast(recyclerView, context, LogsHelper.ON_UNAVAILABLE)
                resetAnimation()
            }

            override fun onAudioStart(placement: String) {
                Log.v(REWARDED_FRAGMENT_TAG, "onAudioStart $placement")
                LogsHelper.logAndToast(recyclerView, context, LogsHelper.ON_AUDTIO_START)
            }

            override fun onAudioFinish(placement: String) {
                Log.v(REWARDED_FRAGMENT_TAG, "onAudioFinish $placement")
                LogsHelper.logAndToast(recyclerView, context, LogsHelper.ON_AUDIO_FINISH)
            }

            override fun onCompletion(placement: String, userRewarded: Boolean) {
                LogsHelper.logAndToast(recyclerView, context, LogsHelper.ON_COMPLETION)
                Log.v(REWARDED_FRAGMENT_TAG, "onCompletion rewarded $placement")
            }

        }
        Rewarded.setRewardedListener(rewardedListener)
    }

    private fun startRequestAnimation() {
        progressBar.visibility = View.VISIBLE
        showButton.isEnabled = false
    }

    private fun onAdAvailableAnimation() {
        showButton.isEnabled = true
        progressBar.visibility = View.GONE
    }

    private fun resetAnimation() {
        progressBar.visibility = View.GONE
        showButton.isEnabled = false
    }

    override fun onFirstLogLine() {
        cleanCallBacks.isEnabled = true
    }


}
