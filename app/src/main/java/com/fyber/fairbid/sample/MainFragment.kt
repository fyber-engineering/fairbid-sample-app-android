package com.fyber.fairbid.sample

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class MainFragment : Fragment() {

    interface FragmentListener {
        fun onButtonClicked(view: View)
    }

    private var bannerButton: Button? = null
    private var interstitialButton: Button? = null
    private var rewardedButton: Button? = null
    private var testSuiteButton: Button? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.main_fragment, container, false)
        initializeUiControls(view)
        return view
    }

    private fun initializeUiControls(view: View) {
        bannerButton = view.findViewById(R.id.banner_button) as Button
        setClickListener(bannerButton!!)
        interstitialButton = view.findViewById(R.id.interstitial_button) as Button
        setClickListener(interstitialButton!!)
        rewardedButton = view.findViewById(R.id.rewarded_button) as Button
        setClickListener(rewardedButton!!)
        testSuiteButton = view.findViewById(R.id.test_suite_button) as Button
        setClickListener(testSuiteButton!!)
    }


    private fun setClickListener(button: Button) {
        button.setOnClickListener {
            (activity as FragmentListener).onButtonClicked(button)
        }
    }
}