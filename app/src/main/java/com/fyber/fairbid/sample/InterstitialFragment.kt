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

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fyber.fairbid.ads.ImpressionData
import com.fyber.fairbid.ads.Interstitial
import com.fyber.fairbid.ads.interstitial.InterstitialListener
import com.fyber.fairbid.utilities.OnScreenCallbacksHelper
import com.fyber.fairbid.utilities.LogsList

/**
 * Log tag
 */
private const val INTERSTITIAL_FRAGMENT_TAG = "InterstitialFragment"

/**
 * A Screen demonstrating how to request and display interstitial ads using the FairBid SDK.
 */
@Composable
fun InterstitialScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val activity = context as? androidx.activity.ComponentActivity
    val interstitialPlacementName = "197405"

    var logs by remember { mutableStateOf(listOf<String>()) }
    var isLoading by remember { mutableStateOf(false) }
    var isAdAvailable by remember { mutableStateOf(false) }

    val addLog = { message: String ->
        logs = logs + "${OnScreenCallbacksHelper.getCurrentTime()} - $message"
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    DisposableEffect(Unit) {
        val interstitialListener = object : InterstitialListener {
            override fun onShow(placement: String, impressionData: ImpressionData) {
                Log.v(INTERSTITIAL_FRAGMENT_TAG, "onShow $placement")
                addLog(OnScreenCallbacksHelper.ON_SHOW)
            }

            override fun onShowFailure(placement: String, impressionData: ImpressionData) {
                Log.v(INTERSTITIAL_FRAGMENT_TAG, "onShowFailure $placement")
                addLog(OnScreenCallbacksHelper.ON_SHOW_FAILURE)
            }

            override fun onRequestStart(placement: String, requestId: String) {
                Log.v(INTERSTITIAL_FRAGMENT_TAG, "onRequestStart $placement - $requestId")
                addLog(OnScreenCallbacksHelper.ON_REQUEST_START)
            }

            override fun onClick(placement: String) {
                Log.v(INTERSTITIAL_FRAGMENT_TAG, "onClick $placement")
                addLog(OnScreenCallbacksHelper.ON_CLICK)
            }

            override fun onHide(placement: String) {
                Log.v(INTERSTITIAL_FRAGMENT_TAG, "onHide $placement")
                addLog(OnScreenCallbacksHelper.ON_HIDE)
            }

            override fun onAvailable(placement: String) {
                Log.v(INTERSTITIAL_FRAGMENT_TAG, "onAvailable $placement")
                addLog(OnScreenCallbacksHelper.ON_AVAILABLE)
                isLoading = false
                isAdAvailable = true
            }

            override fun onUnavailable(placement: String) {
                Log.v(INTERSTITIAL_FRAGMENT_TAG, "onUnavailable $placement")
                addLog(OnScreenCallbacksHelper.ON_UNAVAILABLE)
                isLoading = false
                isAdAvailable = false
            }
        }
        Interstitial.setInterstitialListener(interstitialListener)

        onDispose {
            // Cleanup if needed
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFEFEFF4))
                .padding(top = 10.dp, bottom = 11.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Back",
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.CenterStart)
                    .clickable { onBack() },
                tint = Color.Black
            )

            Text(
                text = stringResource(id = R.string.interstitial_header_name),
                modifier = Modifier.align(Alignment.Center),
                fontSize = 20.sp,
                color = Color(0xFF1D0047),
                lineHeight = 25.8.sp
            )
        }

        Divider(color = Color(0xFFC3C3C3), thickness = 1.dp)

        // Placement info
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 26.dp, end = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.fb_ic_interstitial),
                contentDescription = "Placement icon",
                modifier = Modifier.size(50.dp)
            )

            Column(
                modifier = Modifier.padding(start = 12.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.placement_id),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = interstitialPlacementName,
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
        }

        // Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Request button with progress
            Box(
                modifier = Modifier
                    .weight(0.5f)
                    .height(48.dp)
                    .background(Color(0xFF6A1B9A))
                    .clickable {
                        Log.v(INTERSTITIAL_FRAGMENT_TAG, "Requesting Interstitial")
                        if (!Interstitial.isAvailable(interstitialPlacementName)) {
                            Interstitial.request(interstitialPlacementName)
                            isLoading = true
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(35.dp),
                            color = Color.White,
                            strokeWidth = 3.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                    Text(
                        text = stringResource(id = R.string.request_ad),
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }

            // Show button
            Button(
                onClick = {
                    Log.v(INTERSTITIAL_FRAGMENT_TAG, "Showing Interstitial")
                    activity?.let { Interstitial.show(interstitialPlacementName, it) }
                    isAdAvailable = false
                    isLoading = false
                },
                enabled = isAdAvailable,
                modifier = Modifier
                    .weight(0.5f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF6A1B9A),
                    disabledBackgroundColor = Color(0xFFC5D0DE)
                )
            ) {
                Text(
                    text = stringResource(id = R.string.show_ad),
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }

        // Callbacks list header
        Text(
            text = stringResource(id = R.string.callbacks_list),
            modifier = Modifier.padding(start = 20.dp, top = 15.dp),
            fontSize = 14.sp,
            color = Color.Black
        )

        Divider(
            color = Color(0xFFC3C3C3),
            thickness = 1.dp,
            modifier = Modifier.padding(top = 11.dp)
        )

        // Logs list
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LogsList(logs = logs)
        }

        // Clean button
        Button(
            onClick = { logs = emptyList() },
            enabled = logs.isNotEmpty(),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 30.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF6A1B9A),
                disabledBackgroundColor = Color(0xFFC5D0DE)
            )
        ) {
            Text(
                text = stringResource(id = R.string.clean_callbacks_list),
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}
