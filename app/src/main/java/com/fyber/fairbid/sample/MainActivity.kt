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

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.fyber.FairBid
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


/**
 * The Main Activity,
 * responsible for starting the FairBid SDK and displaying the different examples -
 *
 */
//TODO add comment here publisher id
private const val PUBLISHER_APP_ID = "109613"

class MainActivity : MainFragment.FragmentListener, AppCompatActivity() {

    private val bannerFragment = BannerFragment()
    private val rewardedFragment = RewardedFragment()
    private val interstitialFragment = InterstitialFragment()
    private val mainFragment = MainFragment()
    private var shouldSplashScreen = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CalligraphyConfig.initDefault(
            CalligraphyConfig.Builder().setDefaultFontPath("/fonts/Raleway-Regular.ttf").setFontAttrId(
                R.attr.fontPath
            ).build()
        )
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, SplashScreenFragment()).commit()
        Handler().postDelayed({
            if(shouldSplashScreen) {
                supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.abc_fade_out, R.anim.abc_fade_out)
                    .replace(R.id.fragment_container, mainFragment).commit()
                shouldSplashScreen = false
            }
        }, 2000)
        startFairBidSdk()

    }

    private fun startFairBidSdk() {
        //TODO add comment start sdk
        val fairBid = FairBid.configureForAppId(PUBLISHER_APP_ID).enableLogs()
        fairBid.start(this)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onButtonClicked(id: Int) {
        when (id) {
            R.drawable.banner_icon -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, bannerFragment)
                    .addToBackStack(null).commit()
            }
            R.drawable.rewarded_icon -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, rewardedFragment)
                    .addToBackStack(null).commit()
            }
            R.drawable.interstitial_icon -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, interstitialFragment)
                    .addToBackStack(null).commit()
            }
            R.drawable.ic_test_suite -> {
                //TODO comment show test suite
                FairBid.showTestSuite(this)
            }
        }
    }
}