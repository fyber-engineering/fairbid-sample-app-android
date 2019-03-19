package com.fyber.fairbid.sample

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.fyber.FairBid
import com.fyber.fairbid.ads.Rewarded
import com.fyber.fairbid.ads.rewarded.RewardedListener

//TODO add comment rewaeded placement NAME
private const val REWARDED_PLACEMENT_NAME = "Rewarded"

private const val REWARDED_FRAGMENT_TAG = "RewardedFragment"

class RewardedFragment : Fragment() {

    private var requestButton: Button? = null
    private var showButton: Button? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.rewarded_fragment, container, false)
        initializeUiControls(view)
        setListener()
        return view
    }

    private fun initializeUiControls(view: View) {
        requestButton = view.findViewById(R.id.request_video_rewarded)
        requestButton!!.setOnClickListener {
            requestRewarded()
        }
        showButton = view.findViewById(R.id.show_video_rewarded)
        showButton!!.setOnClickListener {
            showRewaded()
        }
    }

    private fun requestRewarded() {
        Log.v(REWARDED_FRAGMENT_TAG, "Requesting RewardedVideo")
        //TODO add comment Request REWADED PLACEMENT
        Rewarded.request(REWARDED_PLACEMENT_NAME)
    }

    private fun showRewaded() {
        Log.v(REWARDED_FRAGMENT_TAG, "Requesting RewardedVideo")
        //TODO add comment show Interstitial PLACEMENT
        Rewarded.show(REWARDED_PLACEMENT_NAME, activity)
    }

    private fun setListener() {
        //TODO add comment LISTENER
        val rewardedListener = object : RewardedListener {
            override fun onShow(placement: String) {
                Log.v(REWARDED_FRAGMENT_TAG, "onShow $placement")
            }

            override fun onClick(placement: String) {
                Log.v(REWARDED_FRAGMENT_TAG, "onClick $placement")
            }

            override fun onHide(placement: String) {
                Log.v(REWARDED_FRAGMENT_TAG, "onHide $placement")
            }

            override fun onShowFailure(placement: String) {
                Log.v(REWARDED_FRAGMENT_TAG, "onShowFailure $placement")
            }

            override fun onAvailable(placement: String) {
                Log.v(REWARDED_FRAGMENT_TAG, "onAvailable $placement")
            }

            override fun onUnavailable(placement: String) {
                Log.v(REWARDED_FRAGMENT_TAG, "onUnavailable $placement")
            }

            override fun onAudioStart(placement: String) {
                Log.v(REWARDED_FRAGMENT_TAG, "onAudioStart $placement")
            }

            override fun onAudioFinish(placement: String) {
                Log.v(REWARDED_FRAGMENT_TAG, "onAudioFinish $placement")
            }

            override fun onCompletion(placement: String, userRewarded: Boolean) {
                if (userRewarded) {
                    Log.v(REWARDED_FRAGMENT_TAG, "onCompletion rewarded $placement")
                } else {
                    Log.v(REWARDED_FRAGMENT_TAG, "onCompletion not rewarded $placement")
                }
            }

        }

        Rewarded.setRewardedListener(rewardedListener)
    }

}
