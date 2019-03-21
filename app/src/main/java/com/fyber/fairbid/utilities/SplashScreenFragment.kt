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
package com.fyber.fairbid.utilities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fyber.FairBid
import com.fyber.fairbid.sample.R


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
