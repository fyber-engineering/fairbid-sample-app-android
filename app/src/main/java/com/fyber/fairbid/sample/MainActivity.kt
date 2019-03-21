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

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.fyber.FairBid
import com.fyber.fairbid.utilities.MainFragment
import com.fyber.fairbid.utilities.SplashScreenFragment
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


/**
 * The Main Activity,
 * responsible for starting the FairBid SDK and displaying the different examples -
 */
class MainActivity : MainFragment.FragmentListener, AppCompatActivity() {

    companion object {
        /** The app id provided through the Fyber console*/
        private const val PUBLISHER_APP_ID = "109613"
    }


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
        splashScreen()
        startFairBidSdk(PUBLISHER_APP_ID)
    }

    /**
     * Helper method for initializing the SDK with the given app id
     * @param appId The app id provided through the Fyber console
     */
    private fun startFairBidSdk(appId: String) {
        val fairBid = FairBid.configureForAppId(appId).enableLogs()
        fairBid.start(this)
    }

    /**
     * Helper method for showing the test suite
     * @param activity The activity provided by the publisher
     */
    private fun showTestSuite(activity: Activity) {
        FairBid.showTestSuite(activity)
    }

    private fun splashScreen() {
        supportFragmentManager.beginTransaction().add(R.id.fragment_container,
            SplashScreenFragment()
        ).commit()
        Handler().postDelayed({
            if (shouldSplashScreen) {
                supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.abc_fade_out, R.anim.abc_fade_out)
                    .replace(R.id.fragment_container, mainFragment).commitAllowingStateLoss()
                shouldSplashScreen = false
            }
        }, 2000)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onButtonClicked(unitType: MainFragment.UnitType) {
        when (unitType) {
            MainFragment.UnitType.Banner -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, bannerFragment)
                    .addToBackStack(null).commit()
            }
            MainFragment.UnitType.Rewarded -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, rewardedFragment)
                    .addToBackStack(null).commit()
            }
            MainFragment.UnitType.Interstitial -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, interstitialFragment)
                    .addToBackStack(null).commit()
            }
            MainFragment.UnitType.TestSuite -> {
                showTestSuite(this)
            }
        }
    }
}