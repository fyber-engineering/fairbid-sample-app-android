/*
 * Copyright (c) 2022. Fyber N.V
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
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.fyber.FairBid
import com.fyber.fairbid.utilities.MainFragment.UnitType
import com.fyber.fairbid.utilities.MainScreen
import com.fyber.fairbid.utilities.SplashScreen
import kotlinx.coroutines.delay


/**
 * The Main Activity,
 * responsible for starting the FairBid SDK and displaying the different ads - banner, interstitial, rewarded
 */
class MainActivity : ComponentActivity() {

    companion object {
        /**
         * The app id provided through the Fyber console
         * "109613" can be used a sample application.
         * TODO replace with your own app id.
         */
        private const val PUBLISHERS_APP_ID = "109613"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startFairBidSdk(PUBLISHERS_APP_ID)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainApp(
                        onShowTestSuite = { showTestSuite(this) },
                        onNavigateToBanner = { navigateToBanner() },
                        onNavigateToMrec = { navigateToMrec() },
                        onNavigateToRewarded = { navigateToRewarded() },
                        onNavigateToInterstitial = { navigateToInterstitial() }
                    )
                }
            }
        }
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

    private fun navigateToBanner() {
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    BannerScreen(
                        unitType = UnitType.Banner,
                        onBack = { recreate() }
                    )
                }
            }
        }
    }

    private fun navigateToMrec() {
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    BannerScreen(
                        unitType = UnitType.Mrec,
                        onBack = { recreate() }
                    )
                }
            }
        }
    }

    private fun navigateToRewarded() {
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    RewardedScreen(onBack = { recreate() })
                }
            }
        }
    }

    private fun navigateToInterstitial() {
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    InterstitialScreen(onBack = { recreate() })
                }
            }
        }
    }
}

@Composable
fun MainApp(
    onShowTestSuite: () -> Unit,
    onNavigateToBanner: () -> Unit,
    onNavigateToMrec: () -> Unit,
    onNavigateToRewarded: () -> Unit,
    onNavigateToInterstitial: () -> Unit
) {
    var showSplash by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(2000)
        showSplash = false
    }

    AnimatedVisibility(
        visible = showSplash,
        exit = fadeOut()
    ) {
        SplashScreen()
    }

    AnimatedVisibility(
        visible = !showSplash,
        enter = fadeIn()
    ) {
        MainScreen(
            onButtonClicked = { unitType ->
                when (unitType) {
                    UnitType.Banner -> onNavigateToBanner()
                    UnitType.Mrec -> onNavigateToMrec()
                    UnitType.Rewarded -> onNavigateToRewarded()
                    UnitType.Interstitial -> onNavigateToInterstitial()
                    UnitType.TestSuite -> onShowTestSuite()
                }
            }
        )
    }
}
