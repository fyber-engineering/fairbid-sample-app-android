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

import android.app.Activity
import android.content.Context
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
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.RecyclerView
import com.fyber.FairBid
import com.fyber.fairbid.ads.Banner
import com.fyber.fairbid.ads.FairBidListener
import com.fyber.fairbid.ads.ImpressionData
import com.fyber.fairbid.ads.banner.BannerError
import com.fyber.fairbid.ads.banner.BannerListener
import com.fyber.fairbid.ads.banner.BannerOptions
import com.fyber.fairbid.ads.banner.BannerSize
import com.fyber.fairbid.utilities.MainFragment
import com.fyber.fairbid.utilities.OnScreenCallbacksHelper

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
        private const val PLACEMENT_KEY = "PLACEMENT"
        private const val IS_MREC_KEY = "IS_MREC"


        fun createInstance(bannerType: MainFragment.UnitType): BannerFragment {
//            val BANNER_PLACEMENT_NAME = "216936"
            val BANNER_PLACEMENT_NAME = "220161"



//            val MREC_PLACEMENT_NAME = "936586"

            val arguments = Bundle().apply {
                when (bannerType) {
                    MainFragment.UnitType.Banner -> {
                        putString(PLACEMENT_KEY, BANNER_PLACEMENT_NAME)
                        putBoolean(IS_MREC_KEY, false)
                    }
                    MainFragment.UnitType.Mrec -> {
//                        putString(PLACEMENT_KEY, MREC_PLACEMENT_NAME)
                        putBoolean(IS_MREC_KEY, true)
                    }
                    else -> throw IllegalArgumentException("Unsupported banner type: $bannerType")
                }
            }
            return BannerFragment().also {
                it.arguments = arguments
            }
        }
    }

    lateinit var bannerImpressionDataListener: BannerListener
    lateinit var realBannerListener: BannerListener
    private lateinit var loadBannerButton: View
    private lateinit var cleanCallBacks: Button
    private lateinit var destroyBannerButton: Button
    private lateinit var bannerContainer: ViewGroup
    private lateinit var recyclerView: RecyclerView
    private lateinit var placementIcon: ImageView
    private lateinit var progressBar: ProgressBar
    private var fragmentView: View? = null

    private lateinit var bannerPlacementId: String
    private lateinit var bannerSize: BannerSize

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bannerPlacementId = requireArguments().getString(PLACEMENT_KEY, "")
        bannerSize = requireArguments().getBoolean(IS_MREC_KEY)
            .let { isMrec -> if (isMrec) BannerSize.MREC else BannerSize.SMART }

        if (fragmentView == null) {
            fragmentView = inflater.inflate(R.layout.ad_container_fragment, container, false)
            fragmentView?.let {
                initializeUiElements(it)
//                setListener()
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
        Banner.setBannerListener(bannerImpressionDataListener)
        Banner.show(bannerPlacementName, bannerOptions, activity as Activity)
        startRequestAnimation()
    }

    /**
     * Convenience method. Generates a new instance of BannerOptions and configure it accordingly.
     */
    private fun generateBannerOptions(): BannerOptions {
        //Calling the API method BannerOptions().placeInContainer in order to set banner position in the desired view group

        return BannerOptions()
            .setRefreshMode(BannerOptions.RefreshMode.OFF)
            .placeInContainer(bannerContainer)
            .withSize(bannerSize)
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
        bannerContainer.visibility = View.GONE
    }

    /**
     * This function provides an example of Listening to FairBid Banner Callbacks and events.
     */
    open class BannerListenerImpl(
        val recyclerView: RecyclerView,
        val context: Context,
        val onAdAvailableAnimation: () -> Unit,
        val resetAnimation: () -> Unit
    ) : BannerListener {
        override fun onShow(placement: String, impressionData: ImpressionData) {
            Log.v(BANNER_FRAGMENT_TAG, "onShow $placement")
            OnScreenCallbacksHelper.logAndToast(recyclerView, context, OnScreenCallbacksHelper.ON_SHOW)
        }

        override fun onRequestStart(placement: String, requestId: String) {
            Log.v(BANNER_FRAGMENT_TAG, "onRequestStart $placement - $requestId")
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

    /**
     * Internal sample method. initialize the UI elements in this fragment.
     * @param view the container view for this fragment
     */
    private fun initializeUiElements(view: View) {
        initLogRecycler(view)
        initTextViews(view)
        initButtons(view)
        placementIcon = view.findViewById(R.id.placement_icon)
        when (bannerSize) {
            BannerSize.SMART -> R.drawable.fb_ic_banner
            BannerSize.MREC -> R.drawable.fb_ic_mrec
        }.let { placementIcon.setImageResource(it) }
        bannerContainer = view.findViewById(R.id.banner_container)
        bannerContainer.visibility = View.GONE
    }

    /**
     * Internal sample method. initialize the recycler view used to display callbacks and events.
     * @param view the container view for this fragment
     */
    private fun initLogRecycler(view: View) {
        recyclerView = view.findViewById(R.id.recycler_callbacks)
        OnScreenCallbacksHelper.configureRecycler(recyclerView, requireActivity(), this)
        bannerImpressionDataListener = object : BannerListenerImpl(recyclerView, requireContext(), ::onAdAvailableAnimation, ::resetAnimation) {
            override fun onClick(placementId: String) {
                super.onClick(placementId)
                Log.w("BF", "bannerImpressionDataListener: onClick")
            }

            override fun onError(placementId: String, error: BannerError) {
                super.onError(placementId, error)
                Log.w("BF", "bannerImpressionDataListener: onError")
            }

            override fun onLoad(placementId: String) {
                super.onLoad(placementId)
                Log.w("BF", "bannerImpressionDataListener: onLoad")
            }

            override fun onRequestStart(placementId: String, requestId: String) {
                super.onRequestStart(placementId, requestId)
                Log.w("BF", "bannerImpressionDataListener: onRequestStart")
            }

            override fun onShow(placementId: String, impressionData: ImpressionData) {
                super.onShow(placementId, impressionData)
                Log.w("BF", "bannerImpressionDataListener: $impressionData")
                Log.w("BF", "bannerImpressionDataListener: changing the listener")
                Banner.setBannerListener(realBannerListener)
                Log.w("BF", "bannerImpressionDataListener: unveiling the banner")
            }
        }
        realBannerListener = object : BannerListenerImpl(recyclerView, requireContext(), ::onAdAvailableAnimation, ::resetAnimation) {
            override fun onClick(placementId: String) {
                super.onClick(placementId)
                Log.w("BF", "realBannerListener: onClick")
            }

            override fun onError(placementId: String, error: BannerError) {
                super.onError(placementId, error)
                Log.w("BF", "realBannerListener: onError")
            }

            override fun onLoad(placementId: String) {
                super.onLoad(placementId)
                Log.w("BF", "realBannerListener: onLoad")
            }

            override fun onRequestStart(placementId: String, requestId: String) {
                super.onRequestStart(placementId, requestId)
                Log.w("BF", "realBannerListener: onRequestStart")
            }

            override fun onShow(placementId: String, impressionData: ImpressionData) {
                super.onShow(placementId, impressionData)
                Log.w("BF", "realBannerListener: $impressionData")
            }
        }
    }

    /**
     * Internal sample method. initialize the text views in this fragment
     * @param view the container view for this fragment
     */
    private fun initTextViews(view: View) {
        val placementName: TextView = view.findViewById(R.id.placement_name_tv) as TextView
        placementName.text = bannerPlacementId
        val headerName: TextView = view.findViewById(R.id.fragment_header) as TextView
        headerName.text = getString(R.string.banner_header_name)
        val placementIcon: ImageView = view.findViewById(R.id.placement_icon) as ImageView
        placementIcon.background = ContextCompat.getDrawable(requireContext(), R.drawable.fb_ic_banner)
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
            displayBanner(bannerPlacementId)
        }
        destroyBannerButton = view.findViewById(R.id.show_ad)
        destroyBannerButton.background = ContextCompat.getDrawable(requireContext(), R.drawable.button_effect_banner)
        destroyBannerButton.text = getString(R.string.destroy)
        destroyBannerButton.setOnClickListener {
            destroyBanner(bannerPlacementId)
        }
        val backButton = view.findViewById(R.id.back_button) as ImageView
        backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        cleanCallBacks = view.findViewById(R.id.clean_callback_button) as Button
        cleanCallBacks.setOnClickListener {
            cleanCallBacks.isEnabled = false
            OnScreenCallbacksHelper.clearLog(recyclerView)
        }
        view.findViewById<Button>(R.id.show_hide_container_view_button).setOnClickListener {
            bannerContainer.flipVisibility()
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

private fun View.flipVisibility() {
    val newVisibility = when (this.visibility) {
        View.GONE,
        View.INVISIBLE -> View.VISIBLE
        View.VISIBLE -> View.INVISIBLE
        else -> throw IllegalArgumentException("unexpected argument")
    }
    this.visibility = newVisibility
}
