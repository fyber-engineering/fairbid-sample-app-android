Fyber's FairBid - Sample Android application
============================================
This sample app demonstrates the FairBid SDK integration best practices. It is aligned with the FairBid developer documentation - developer.fyber.com, which we recommend you to read as well.

Please note that when it comes to actually demonstrating the product "FairBid" as a monetization platform this sample app might fail due to the fact that ad delivery depends on factors like your country, your device your AAID/IDFA or any other kind of information that ad networks might have collected about you and that are taken into consideration when placing a bid to show an ad to you.

#### Prerequisites
* Minimum SDK 19 (KitKat 4.4) 
* Android build plugin 3.4.2 / Gradle 5.4.1 / Kotlin 1.3.61 

#### Navigating the sample code
* SDK Initialization is located in the [MainActivity](https://github.com/Heyzap/fairbid-sample-app-android/blob/master/app/src/main/java/com/fyber/fairbid/sample/MainActivity.kt)
* Requesting Banner Ads - [BannerFragment](https://github.com/Heyzap/fairbid-sample-app-android/blob/master/app/src/main/java/com/fyber/fairbid/sample/BannerFragment.kt)
* Requesting Interstitial Ads - [InterstitialFragment](https://github.com/Heyzap/fairbid-sample-app-android/blob/master/app/src/main/java/com/fyber/fairbid/sample/InterstitialFragment.kt)
* Requesting Rewarded Ads - [RewardedFragment](https://github.com/Heyzap/fairbid-sample-app-android/blob/master/app/src/main/java/com/fyber/fairbid/sample/RewardedFragment.kt)

#### Support and documentation
Please visit our [documentation](https://dev-android.fyber.com/docs)

#### Project License

    Copyright (c) 2022. Fyber N.V
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
         
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
