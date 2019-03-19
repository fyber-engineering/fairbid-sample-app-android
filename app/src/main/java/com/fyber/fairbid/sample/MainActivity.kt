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
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.fyber.FairBid


/**
 * The Main Activity,
 * responsible for starting the FairBid SDK and displaying the different examples -
 *
 */
//TODO add comment here publisher id
private const val PUBLISHER_APP_ID = "109613"

class MainActivity : MainFragment.FragmentListener, AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragment = MainFragment()
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit()
        //TODO add comment start sdk
        val fairBid = FairBid.configureForAppId(PUBLISHER_APP_ID).enableLogs()
        fairBid.start(this)
    }

    override fun onButtonClicked(view: View) {
        when (view.id) {
            R.id.banner_button -> Log.v("MainFragment - ", "banner clicked");
            R.id.rewarded_button -> {
                val rewardedFragment = RewardedFragment()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, rewardedFragment)
                    .addToBackStack(null).commit()
            };
            R.id.interstitial_button -> {
                val interstitialFragment = InterstitialFragment()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, interstitialFragment)
                    .addToBackStack(null).commit()
            }
            R.id.test_suite_button -> {Log.v("MainFragment - ", "test_suite_button clicked");
                FairBid.showTestSuite(this)
            }
            else -> { // Note the block
                Log.v("MainFragment - ", "else")
            }
        }
    }

    private fun replaceFragment()
    {

    }


}