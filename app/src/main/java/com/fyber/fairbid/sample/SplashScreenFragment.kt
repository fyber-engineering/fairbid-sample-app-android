package com.fyber.fairbid.sample

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fyber.FairBid


/**
 * A simple {@link Fragment} subclass.
 */
class SplashScreenFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.splash_screen_fragment, container, false)
        val sdkVersion: TextView = view.findViewById(R.id.sdk_version)
        sdkVersion.text = getString(R.string.fyber_fairbid) + " " + FairBid.SDK_VERSION
        return view
    }
}
