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

//TODO add comment rewaeded placement NAME
private const val REWARDED_PLACEMENT_NAME = "Rewarded"
private const val REWARDED_FRAGMENT_HEADER = "Rewarded"
private const val REWARDED_FRAGMENT_TAG = "RewardedFragment"

class RewardedFragment : Fragment(), MainFragment.LogsListener {

    private lateinit var requestButton: View
    private lateinit var cleanCallBacks: Button
    private lateinit var showButton: Button
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
            requestRewarded()
        }
        showButton = view.findViewById(R.id.show_ad)
        showButton.setOnClickListener {
            showRewaded()
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

    private fun requestRewarded() {
        Log.v(REWARDED_FRAGMENT_TAG, "Requesting RewardedVideo")
        //TODO add comment Request REWADED PLACEMENT
        Rewarded.request(REWARDED_PLACEMENT_NAME)
        startRequestAnimiaon()
    }

    private fun showRewaded() {
        Log.v(REWARDED_FRAGMENT_TAG, "Requesting RewardedVideo")
        //TODO add comment show Interstitial PLACEMENT
        Rewarded.show(REWARDED_PLACEMENT_NAME, activity)
        resetAnimation()
    }

    private fun setListener() {
        //TODO add comment LISTENER
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
                onAdAvilabileAnimation()
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

    private fun startRequestAnimiaon() {
        progressBar.visibility = View.VISIBLE
        showButton.isEnabled = false
    }

    private fun onAdAvilabileAnimation() {
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
