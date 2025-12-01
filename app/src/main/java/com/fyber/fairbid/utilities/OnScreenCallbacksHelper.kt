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
package com.fyber.fairbid.utilities

import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Utility helper, displays callback events on screen using a lazy column
 */
class OnScreenCallbacksHelper {

    companion object {
        //Callbacks
        const val ON_SHOW = "onShow()"
        const val ON_CLICK = "onClick()"
        const val ON_HIDE = "onHide()"
        const val ON_REQUEST_START = "onRequestStart()"
        const val ON_SHOW_FAILURE = "onShowFailure()"
        const val ON_AVAILABLE = "onAvailable()"
        const val ON_UNAVAILABLE = "onUnavailable()"
        const val ON_AUDIO_START = "onAudioStart()"
        const val ON_AUDIO_FINISH = "onAudioFinish()"
        const val ON_COMPLETION = "onCompletion()"
        const val ON_ERROR = "onError()"
        const val ON_LOAD = "onLoad()"

        fun getCurrentTime(): String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
            current.format(formatter)
        } else {
            val date = Date()
            val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            formatter.format(date)
        }
    }
}

@Composable
fun LogsList(logs: List<String>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(logs) { log ->
            LogRow(log = log)
        }
    }
}

@Composable
fun LogRow(log: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(34.dp)
    ) {
        Text(
            text = log,
            modifier = Modifier.padding(start = 16.dp, top = 6.dp, bottom = 6.dp),
            color = Color.Black,
            fontSize = 13.sp
        )

        Divider(
            color = Color(0xFFC3C3C3),
            thickness = 1.dp,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun ShowToast(message: String) {
    val context = LocalContext.current
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
