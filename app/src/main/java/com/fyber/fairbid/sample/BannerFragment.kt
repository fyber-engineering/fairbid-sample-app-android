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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.fyber.fairbid.ads.Banner
import com.fyber.fairbid.ads.banner.BannerError
import com.fyber.fairbid.ads.banner.BannerListener
import com.fyber.fairbid.ads.banner.BannerOptions

//TODO add comment banner placement NAME
private const val BANNER_PLACEMENT_NAME = "Banner"

private const val BANNER_FRAGMENT_TAG = "BannerFragment"


class BannerFragment : Fragment() {

    private lateinit var loadBannerButton: Button
    private lateinit var destroyBannerButton: Button
    private lateinit var bannerContainer: ViewGroup


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.banner_fragment, container, false)
        initializeUiControls(view)
        setListener()
        return view
    }

    private fun initializeUiControls(view: View) {
        loadBannerButton = view.findViewById(R.id.btn_loadbanner)
        loadBannerButton.setOnClickListener {
            displayBanner()
        }

        destroyBannerButton = view.findViewById(R.id.btn_destroybanner)
        destroyBannerButton.setOnClickListener {
            destroyBanner()
        }

        bannerContainer = view.findViewById(R.id.banner_container)
    }

    private fun displayBanner() {
        //TODO added comment display banner
        Log.v(BANNER_FRAGMENT_TAG, "displayBanner()")
        val bannerOptions: BannerOptions = getBannerOptions()
        Banner.display(BANNER_PLACEMENT_NAME, bannerOptions, activity)
    }

    //TODO add comment this function is OPTIONAL, default is bottom
    private fun getBannerOptions(): BannerOptions {
        return BannerOptions().placeInContainer(bannerContainer)
    }

    private fun destroyBanner() {
        //TODO added comment destroy banner
        Log.v(BANNER_FRAGMENT_TAG, "destroyBanner()")
        Banner.destroy(BANNER_PLACEMENT_NAME)
    }

    private fun setListener() {
        //TODO add comment LISTENER
        val bannerListener = object : BannerListener {
            override fun onClick(placement: String) {
                Log.v(BANNER_FRAGMENT_TAG, "onClick $placement")
            }

            override fun onLoad(placement: String) {
                Log.v(BANNER_FRAGMENT_TAG, "onLoad $placement")
            }

            override fun onError(placement: String, error: BannerError) {
                Log.v(BANNER_FRAGMENT_TAG, "onerror $placement , error :" + error.errorMessage)
            }

            override fun onShow(placement: String) {
                Log.v(BANNER_FRAGMENT_TAG, "onShow $placement")
            }
        }
        Banner.setBannerListener(bannerListener)
    }
}