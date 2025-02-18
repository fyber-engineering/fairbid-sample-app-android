package com.fyber.fairbid.sample

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.fyber.fairbid.ads.ImpressionData
import com.fyber.fairbid.ads.banner.BannerError
import com.fyber.fairbid.ads.banner.BannerListener
import com.fyber.fairbid.ads.banner.BannerSize
import com.fyber.fairbid.ads.banner.BannerView
import com.fyber.fairbid.utilities.MainFragment
import com.fyber.fairbid.utilities.OnScreenCallbacksHelper

private const val BANNER_FRAGMENT_TAG = "BannerViewFragment"
/**
 * A Fragment demonstrating how to request and display banner ads using the FairBid SDK.
 */
class BannerViewFragment : Fragment(), OnScreenCallbacksHelper.LogsListener {

    companion object {
        /**
         * The Banner's placement name - as configured at Fyber console
         * "BannerPlacementIdExample" can be used using the provided example APP_ID
         * TODO change to your own configured placement.
         */
        private const val PLACEMENT_KEY = "PLACEMENT"
        private const val IS_MREC_KEY = "IS_MREC"

        fun createInstance(bannerType: MainFragment.UnitType): BannerViewFragment {
            val BANNER_PLACEMENT_NAME = "197407"

            val arguments = Bundle().apply {
                when (bannerType) {
                    MainFragment.UnitType.Banner -> {
                        putString(PLACEMENT_KEY, BANNER_PLACEMENT_NAME)
                        putBoolean(IS_MREC_KEY, false)
                    }
                    else -> throw IllegalArgumentException("Unsupported banner type: $bannerType")
                }
            }
            return BannerViewFragment().also {
                it.arguments = arguments
            }
        }
    }

    private lateinit var loadBannerButton: View
    private lateinit var cleanCallBacks: Button
    private lateinit var destroyBannerButton: Button
    private lateinit var bannerContainer: ViewGroup
    private lateinit var recyclerView: RecyclerView
    private lateinit var placementIcon: ImageView
    private lateinit var progressBar: ProgressBar
    private var fragmentView: View? = null

    private lateinit var bannerPlacementId: String
    private lateinit var bannerSize: BannerSize

    private var bannerView: BannerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bannerPlacementId = requireArguments().getString(PLACEMENT_KEY, "")
        bannerSize = requireArguments().getBoolean(IS_MREC_KEY)
            .let { isMrec -> if (isMrec) BannerSize.MREC else BannerSize.SMART }

        if (fragmentView == null) {
            fragmentView = inflater.inflate(R.layout.banner_view_container_fragment, container, false)
            fragmentView?.let {
                initializeUiElements(it)
            }
        }

        return fragmentView
    }

    /**
     * Called when the loadBannerButton is clicked
     * This function provides an example for adding the BannerView instance to the container view
     */
    private fun displayBanner() {
        Log.v(BANNER_FRAGMENT_TAG, "displayBanner()")
        bannerView = BannerView(requireContext(), bannerPlacementId).also {
            setListener(it)
            it.load()
        }
    }

    /**
     * Called when the destroyBannerButton is clicked
     * This function provides an example for calling the API method Banner.destroy in order to destroy a banner placement
     */
    private fun destroyBanner() {
        Log.v(BANNER_FRAGMENT_TAG, "destroyBanner()")
        bannerView?.destroy()
        bannerContainer.removeView(bannerView)

        resetAnimation()
        loadBannerButton.isEnabled = true
    }

    /**
     * This function provides an example of Listening to FairBid Banner Callbacks and events.
     */
    private fun setListener(bannerView: BannerView) {
        val bannerListener = object : BannerListener {
            override fun onShow(placementId: String, impressionData: ImpressionData) {
                Log.v(BANNER_FRAGMENT_TAG, "onShow $placementId")
                loadBannerButton.isEnabled = false
                OnScreenCallbacksHelper.logAndToast(
                    recyclerView,
                    context,
                    OnScreenCallbacksHelper.ON_SHOW
                )
            }

            override fun onRequestStart(placementId: String, requestId: String) {
                Log.v(BANNER_FRAGMENT_TAG, "onRequestStart $placementId - $requestId")
                OnScreenCallbacksHelper.logAndToast(
                    recyclerView,
                    context,
                    OnScreenCallbacksHelper.ON_REQUEST_START
                )
            }

            override fun onClick(placementId: String) {
                Log.v(BANNER_FRAGMENT_TAG, "onClick $placementId")
                OnScreenCallbacksHelper.logAndToast(
                    recyclerView,
                    context,
                    OnScreenCallbacksHelper.ON_CLICK
                )
            }

            override fun onLoad(placementId: String) {
                Log.v(BANNER_FRAGMENT_TAG, "onLoad $placementId")
                OnScreenCallbacksHelper.logAndToast(
                    recyclerView,
                    context,
                    OnScreenCallbacksHelper.ON_LOAD
                )
                onAdAvailableAnimation()
                bannerContainer.addView(bannerView)
            }

            override fun onError(placementId: String, error: BannerError) {
                Log.v(BANNER_FRAGMENT_TAG, "onError $placementId, error:" + error.errorMessage)
                OnScreenCallbacksHelper.logAndToast(
                    recyclerView,
                    context,
                    OnScreenCallbacksHelper.ON_ERROR + ": " + error.errorMessage
                )
                resetAnimation()
            }

        }
        bannerView.bannerListener = bannerListener
    }

    /**
     * Internal sample method. initialize the UI elements in this fragment.
     * @param view the container view for this fragment
     */
    private fun initializeUiElements(view: View) {
        initLogRecycler(view)
        initTextViews(view)
        initButtons(view)
        placementIcon = view.findViewById(R.id.placement_icon)
        when (bannerSize) {
            BannerSize.SMART -> R.drawable.fb_ic_banner
            BannerSize.MREC -> R.drawable.fb_ic_mrec
        }.let { placementIcon.setImageResource(it) }
        bannerContainer = view.findViewById(R.id.banner_container)
        bannerContainer.visibility = View.VISIBLE
    }

    /**
     * Internal sample method. initialize the recycler view used to display callbacks and events.
     * @param view the container view for this fragment
     */
    private fun initLogRecycler(view: View) {
        recyclerView = view.findViewById(R.id.recycler_callbacks)
        OnScreenCallbacksHelper.configureRecycler(recyclerView, requireActivity(), this)
    }

    /**
     * Internal sample method. initialize the text views in this fragment
     * @param view the container view for this fragment
     */
    private fun initTextViews(view: View) {
        val placementName: TextView = view.findViewById(R.id.placement_name_tv) as TextView
        placementName.text = bannerPlacementId
        val headerName: TextView = view.findViewById(R.id.fragment_header) as TextView
        headerName.text = getString(R.string.banner_header_name)
        val placementIcon: ImageView = view.findViewById(R.id.placement_icon) as ImageView
        placementIcon.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.fb_ic_banner)
    }

    /**
     * Internal sample method. initialize the buttons and click listeners in this fragment
     * @param view the container view for this fragment
     */
    private fun initButtons(view: View) {
        progressBar = view.findViewById(R.id.progress_bar)
        val textView: TextView = view.findViewById(R.id.request_ad)
        textView.text = getString(R.string.show)
        loadBannerButton = view.findViewById(R.id.text_progress_bar)

        loadBannerButton.setOnClickListener {
            displayBanner()
        }
        destroyBannerButton = view.findViewById(R.id.show_ad)
        destroyBannerButton.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.button_effect_banner)
        destroyBannerButton.text = getString(R.string.destroy)
        destroyBannerButton.setOnClickListener {
            destroyBanner()
        }
        val backButton: ImageView = view.findViewById(R.id.back_button)
        backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        cleanCallBacks = view.findViewById(R.id.clean_callback_button)!!
        cleanCallBacks.setOnClickListener {
            cleanCallBacks.isEnabled = false
            OnScreenCallbacksHelper.clearLog(recyclerView)
        }
    }

    /**
     * Internal sample method.
     * Stops the request/loading animation and enables destroying the banner
     */
    private fun onAdAvailableAnimation() {
        destroyBannerButton.isEnabled = true
        progressBar.visibility = View.GONE
    }

    /**
     * Internal sample method.
     * Resets the UI state for the progress animation / destroy button
     */
    private fun resetAnimation() {
        destroyBannerButton.isEnabled = false
        progressBar.visibility = View.GONE
    }

    /**
     * Invoked when the on-screen log became non-empty. used to enable/disable the clean button
     */
    override fun onFirstLogLine() {
        cleanCallBacks.isEnabled = true
    }
}