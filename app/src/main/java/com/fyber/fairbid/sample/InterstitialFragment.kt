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
import android.widget.TextView
import com.fyber.fairbid.ads.Interstitial
import com.fyber.fairbid.ads.interstitial.InterstitialListener

//TODO add comment rewaeded placement NAME
private const val INTERSTITIAL_PLACEMENT_NAME = "Interstitial"
private const val INTERSTITIAL_FRAGMENT_HEADER = "Interstitial"
private const val INTERSTITIAL_FRAGMENT_TAG = "InterstitialFragment"

class InterstitialFragment : Fragment() {

    private lateinit var requestButton: Button
    private lateinit var showButton: Button
    private lateinit var recyclerView: RecyclerView

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
        LogsHelper.configureRecycler(recyclerView, activity!!)
    }

    private fun initButtons(view: View) {
        requestButton = view.findViewById(R.id.request_ad)
        requestButton.setOnClickListener {
            requestInterstitial()
        }
        showButton = view.findViewById(R.id.show_ad)
        showButton.setOnClickListener {
            showInterstitial()
        }
        val backButton: ImageView = view.findViewById(R.id.back_button) as ImageView
        backButton.setOnClickListener {
            activity!!.onBackPressed()
        }

        val cleanCallBacks: Button = view.findViewById(R.id.clean_callback_button) as Button
        cleanCallBacks.setOnClickListener {
            LogsHelper.clearLog(recyclerView)
        }
    }

    private fun initTextViews(view: View) {
        val placementName: TextView = view.findViewById(R.id.placement_name_tv) as TextView
        placementName.text = INTERSTITIAL_PLACEMENT_NAME
        val headerName: TextView = view.findViewById(R.id.fragment_header) as TextView
        headerName.text = INTERSTITIAL_FRAGMENT_HEADER
        val placementIcon: ImageView = view.findViewById(R.id.placement_icon) as ImageView
        placementIcon.background = context!!.getDrawable(R.drawable.interstitial_icon)
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
                Log.v(INTERSTITIAL_FRAGMENT_TAG, "onShow $placement")
                LogsHelper.logAndToast(recyclerView, context, LogsHelper.ON_SHOW)
            }

            override fun onClick(placement: String) {
                Log.v(INTERSTITIAL_FRAGMENT_TAG, "onClick $placement")
                LogsHelper.logAndToast(recyclerView, context, LogsHelper.ON_CLICK)
            }

            override fun onHide(placement: String) {
                Log.v(INTERSTITIAL_FRAGMENT_TAG, "onHide $placement")
                LogsHelper.logAndToast(recyclerView, context, LogsHelper.ON_HIDE)
            }

            override fun onShowFailure(placement: String) {
                Log.v(INTERSTITIAL_FRAGMENT_TAG, "onShowFailure $placement")
                LogsHelper.logAndToast(recyclerView, context, LogsHelper.ON_SHOW_FAILURE)

            }

            override fun onAvailable(placement: String) {
                Log.v(INTERSTITIAL_FRAGMENT_TAG, "onAvailable $placement")
                LogsHelper.logAndToast(recyclerView, context, LogsHelper.ON_AVALIABLE)
            }

            override fun onUnavailable(placement: String) {
                Log.v(INTERSTITIAL_FRAGMENT_TAG, "onUnavailable $placement")
                LogsHelper.logAndToast(recyclerView, context, LogsHelper.ON_UNAVAILABLE)
            }

            override fun onAudioStart(placement: String) {
                Log.v(INTERSTITIAL_FRAGMENT_TAG, "onAudioStart $placement")
                LogsHelper.logAndToast(recyclerView, context, LogsHelper.ON_AUDTIO_START)
            }

            override fun onAudioFinish(placement: String) {
                Log.v(INTERSTITIAL_FRAGMENT_TAG, "onAudioFinish $placement")
                LogsHelper.logAndToast(recyclerView, context, LogsHelper.ON_AUDIO_FINISH)
            }
        }
        Interstitial.setInterstitialListener(interstitialListener)
    }

}
