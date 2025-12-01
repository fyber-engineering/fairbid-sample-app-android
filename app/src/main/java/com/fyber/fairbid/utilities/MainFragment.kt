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

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fyber.FairBid
import com.fyber.fairbid.sample.R

/**
 * Interface for letting the activity know when the user has made some choice
 */
interface FragmentListener {
    /**
     * Called when the user has clicked some unit type.
     * @param unitType The clicked unit type
     */
    fun onButtonClicked(unitType: MainFragment.UnitType)
}

/**
 * Internal class for displaying the sample with multiple choices.
 * This fragment does not contain any sample code for FairBid
 */
class MainFragment {

    /**
     * Enum describing the possible choices in the sample application
     */
    enum class UnitType { Interstitial, Rewarded, Banner, Mrec, TestSuite }

    /**
     * Enum to help differentiate between items and separators
     */
    enum class RowType { Row, Separator }

    /**
     * a model for the displayed items
     * @param unitText the display name for this row
     * @param resourceImage the image to display for this row
     * @param unitType the corresponding {@link UnitType}
     */
    data class UnitRowData(val unitText: String, val resourceImage: Int, val unitType: UnitType)

    /**
     * a model for the rows inside the recycler view
     * @param type the Row Type
     * @property payload the object describing this row, if any
     */
    data class Row(val type: RowType = RowType.Row, val payload: Any? = null)
}

@Composable
fun MainScreen(onButtonClicked: (MainFragment.UnitType) -> Unit) {
    val units = listOf(
        MainFragment.Row(
            payload = MainFragment.UnitRowData(
                "Banner",
                R.drawable.fb_ic_banner,
                MainFragment.UnitType.Banner
            )
        ),
        MainFragment.Row(
            payload = MainFragment.UnitRowData(
                "MREC Banner",
                R.drawable.fb_ic_mrec,
                MainFragment.UnitType.Mrec
            )
        ),
        MainFragment.Row(
            payload = MainFragment.UnitRowData(
                "Interstitial",
                R.drawable.fb_ic_interstitial,
                MainFragment.UnitType.Interstitial
            )
        ),
        MainFragment.Row(
            payload = MainFragment.UnitRowData(
                "Rewarded",
                R.drawable.fb_ic_rewarded,
                MainFragment.UnitType.Rewarded
            )
        ),
        MainFragment.Row(type = MainFragment.RowType.Separator),
        MainFragment.Row(
            payload = MainFragment.UnitRowData(
                "Test Suite",
                R.drawable.fb_ic_test_suite,
                MainFragment.UnitType.TestSuite
            )
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFEFF4))
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFD1F8F8F8))
                .padding(start = 20.dp)
        ) {
            Text(
                text = stringResource(id = R.string.sample_app),
                fontSize = 32.sp,
                color = Color.Black,
                modifier = Modifier.padding(top = 0.dp)
            )
            Text(
                text = "${stringResource(id = R.string.dt_fairbid)} ${FairBid.SDK_VERSION}".uppercase(),
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                modifier = Modifier.padding(bottom = 13.dp),
                fontSize = 14.sp
            )
        }

        Divider(
            color = Color(0xFFC3C3C3),
            thickness = 2.dp
        )

        // List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            items(units) { row ->
                when (row.type) {
                    MainFragment.RowType.Row -> {
                        val unitRowData = row.payload as MainFragment.UnitRowData
                        UnitRow(
                            unitRowData = unitRowData,
                            onClick = { onButtonClicked(unitRowData.unitType) }
                        )
                    }
                    MainFragment.RowType.Separator -> {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .background(Color(0xFFEFEFF4))
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UnitRow(unitRowData: MainFragment.UnitRowData, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(85.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 0.dp, top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = unitRowData.resourceImage),
                contentDescription = "icon",
                modifier = Modifier.size(50.dp)
            )

            Text(
                text = unitRowData.unitText,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp),
                color = Color(0xFF1D0047),
                fontSize = 15.sp,
                lineHeight = 19.35.sp
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "arrow",
                modifier = Modifier.size(30.dp, 40.dp),
                tint = Color.Gray
            )
        }

        Divider(
            color = Color(0xFFC3C3C3),
            thickness = 1.dp,
            modifier = Modifier.padding(start = 20.dp)
        )
    }
}
