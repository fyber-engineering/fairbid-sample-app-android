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
import android.view.Gravity
import android.widget.FrameLayout
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.fyber.fairbid.ads.Banner
import com.fyber.fairbid.ads.ImpressionData
import com.fyber.fairbid.ads.banner.BannerError
import com.fyber.fairbid.ads.banner.BannerListener
import com.fyber.fairbid.ads.banner.BannerOptions
import com.fyber.fairbid.ads.banner.BannerSize
import com.fyber.fairbid.utilities.MainFragment
import com.fyber.fairbid.utilities.OnScreenCallbacksHelper
import com.fyber.fairbid.utilities.LogsList

/**
 * Log tag
 */
private const val BANNER_FRAGMENT_TAG = "BannerFragment"

/**
 * A Screen demonstrating how to request and display banner ads using the FairBid SDK.
 */
@Composable
fun BannerScreen(unitType: MainFragment.UnitType, onBack: () -> Unit) {
    val context = LocalContext.current
    val activity = context as? androidx.activity.ComponentActivity

    val (bannerPlacementId, isMrec) = when (unitType) {
        MainFragment.UnitType.Banner -> "197407" to false
        MainFragment.UnitType.Mrec -> "936586" to true
        else -> throw IllegalArgumentException("Unsupported banner type: $unitType")
    }

    val bannerSize = if (isMrec) BannerSize.MREC else BannerSize.SMART

    var logs by remember { mutableStateOf(listOf<String>()) }
    var isLoading by remember { mutableStateOf(false) }
    var isAdAvailable by remember { mutableStateOf(false) }
    var bannerContainer by remember { mutableStateOf<FrameLayout?>(null) }

    val addLog = { message: String ->
        logs = logs + "${OnScreenCallbacksHelper.getCurrentTime()} - $message"
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    DisposableEffect(Unit) {
        val bannerListener = object : BannerListener {
            override fun onShow(placement: String, impressionData: ImpressionData) {
                Log.v(BANNER_FRAGMENT_TAG, "onShow $placement")
                addLog(OnScreenCallbacksHelper.ON_SHOW)
            }

            override fun onRequestStart(placement: String, requestId: String) {
                Log.v(BANNER_FRAGMENT_TAG, "onRequestStart $placement - $requestId")
                addLog(OnScreenCallbacksHelper.ON_REQUEST_START)
            }

            override fun onClick(placement: String) {
                Log.v(BANNER_FRAGMENT_TAG, "onClick $placement")
                addLog(OnScreenCallbacksHelper.ON_CLICK)
            }

            override fun onLoad(placement: String) {
                Log.v(BANNER_FRAGMENT_TAG, "onLoad $placement")
                addLog(OnScreenCallbacksHelper.ON_LOAD)
                isLoading = false
                isAdAvailable = true
            }

            override fun onError(placement: String, error: BannerError) {
                Log.v(BANNER_FRAGMENT_TAG, "onError $placement, error:" + error.errorMessage)
                addLog("${OnScreenCallbacksHelper.ON_ERROR}: ${error.errorMessage}")
                isLoading = false
                isAdAvailable = false
            }
        }
        Banner.setBannerListener(bannerListener)

        onDispose {
            Banner.destroy(bannerPlacementId)
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
                text = stringResource(id = R.string.banner_header_name),
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
                painter = painterResource(
                    id = if (isMrec) R.drawable.fb_ic_mrec else R.drawable.fb_ic_banner
                ),
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
                    text = bannerPlacementId,
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
            // Request/Show button with progress
            Box(
                modifier = Modifier
                    .weight(0.5f)
                    .height(48.dp)
                    .background(Color(0xFF6A1B9A))
                    .clickable {
                        Log.v(BANNER_FRAGMENT_TAG, "displayBanner()")
                        bannerContainer?.let { container ->
                            val bannerOptions = BannerOptions()
                                .placeInContainer(container)
                                .withSize(bannerSize)
                            activity?.let { Banner.show(bannerPlacementId, bannerOptions, it) }
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
                        text = stringResource(id = R.string.show),
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }

            // Destroy button
            Button(
                onClick = {
                    Log.v(BANNER_FRAGMENT_TAG, "destroyBanner()")
                    Banner.destroy(bannerPlacementId)
                    isLoading = false
                    isAdAvailable = false
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
                    text = stringResource(id = R.string.destroy),
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }

        // Banner container
        AndroidView(
            factory = { ctx ->
                FrameLayout(ctx).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = Gravity.CENTER_HORIZONTAL
                    }
                    bannerContainer = this
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 20.dp)
        )

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
