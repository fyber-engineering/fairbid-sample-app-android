package com.fyber.fairbid.sample

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.fyber.fairbid.ads.Interstitial
import com.fyber.fairbid.ads.interstitial.InterstitialListener

//TODO add comment rewaeded placement NAME
private const val INTERSTITIAL_PLACEMENT_NAME = "Interstitial"

private const val INTERSTITIAL_FRAGMENT_TAG = "InterstitialFragment"

class InterstitialFragment : Fragment() {
    //Buttons
    private lateinit var requestButton: Button
    private lateinit var showButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.interstitial_fragment, container, false)
        initializeUiControls(view)
        setListener()
        return view
    }

    private fun initializeUiControls(view: View) {
        requestButton = view.findViewById(R.id.request_interstitial)
        requestButton.setOnClickListener {
            requestInterstitial()
        }
        showButton = view.findViewById(R.id.show_interstitial)
        showButton.setOnClickListener {
            showInterstitial()
        }
    }

    private fun requestInterstitial() {
        Log.v(INTERSTITIAL_FRAGMENT_TAG, "Requesting Interstitial")
        //TODO add comment Request Interstitial PLACEMENT
        Interstitial.request(INTERSTITIAL_PLACEMENT_NAME)
    }

    private fun showInterstitial() {
        Log.v(INTERSTITIAL_FRAGMENT_TAG, "Requesting Interstitial")
        //TODO add comment Request Interstitial PLACEMENT
        Interstitial.show(INTERSTITIAL_PLACEMENT_NAME, activity)
    }

    private fun setListener() {
        //TODO add comment LISTENER
        val interstitialListener = object : InterstitialListener {
            override fun onShow(placement: String) {
                Toast.makeText(context, "asda", Toast.LENGTH_SHORT).show()
                //Log.v(INTERSTITIAL_FRAGMENT_TAG, "onShow $placement")
            }

            override fun onClick(placement: String) {
                Log.v(INTERSTITIAL_FRAGMENT_TAG, "onClick $placement")
            }

            override fun onHide(placement: String) {
                Log.v(INTERSTITIAL_FRAGMENT_TAG, "onHide $placement")
            }

            override fun onShowFailure(placement: String) {
                Log.v(INTERSTITIAL_FRAGMENT_TAG, "onShowFailure $placement")
            }

            override fun onAvailable(placement: String) {
                Log.v(INTERSTITIAL_FRAGMENT_TAG, "onAvailable $placement")
            }

            override fun onUnavailable(placement: String) {
                Log.v(INTERSTITIAL_FRAGMENT_TAG, "onUnavailable $placement")
            }

            override fun onAudioStart(placement: String) {
                Log.v(INTERSTITIAL_FRAGMENT_TAG, "onAudioStart $placement")
            }

            override fun onAudioFinish(placement: String) {
                Log.v(INTERSTITIAL_FRAGMENT_TAG, "onAudioFinish $placement")
            }


        }
        Interstitial.setInterstitialListener(interstitialListener)
    }

}
