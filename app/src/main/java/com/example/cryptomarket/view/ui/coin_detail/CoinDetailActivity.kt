package com.example.cryptomarket.view.ui.coin_detail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.cryptomarket.R
import com.example.cryptomarket.bases.BaseActivity
import com.example.cryptomarket.constants.AppConstants
import com.example.cryptomarket.data.models.CoinMarket
import com.example.cryptomarket.data.models.Resource
import com.example.cryptomarket.data.models.local.CoinDetailArgs
import com.example.cryptomarket.databinding.ActivityCoinDetailBinding
import com.example.cryptomarket.viewmodel.CoinDetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson

class CoinDetailActivity : BaseActivity(), TabLayout.OnTabSelectedListener, View.OnClickListener {

    private lateinit var binding: ActivityCoinDetailBinding
    private var isShowShimmer = false
    private var isShowError = false
    private var isBackToPrevious = false
    private lateinit var args: CoinDetailArgs
    private val viewModel: CoinDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        binding.detailTabLayout.addOnTabSelectedListener(this)
        replaceFragment(
            PriceChartFragment.newInstance(),
            isAddToBackStack = false,
            anim = false
        )
        showShimmer()
        observeCoinDetail()
        observeWatchList()
        geBundle()
    }

    private fun showShimmer() {
        hideError()
        if (isShowShimmer) return
        isShowShimmer = true
        binding.coinDetailShimmer.root.visibility = View.VISIBLE
        binding.coinDetailShimmer.shimmerLayout.startShimmerAnimation()
        binding.detailTabLayout.visibility = View.GONE
        binding.frmContainer.visibility = View.GONE
    }

    private fun hideShimmer() {
        if (!isShowShimmer) return
        isShowShimmer = false
        binding.coinDetailShimmer.root.visibility = View.GONE
        binding.coinDetailShimmer.shimmerLayout.stopShimmerAnimation()
        binding.detailTabLayout.visibility = View.VISIBLE
        binding.frmContainer.visibility = View.VISIBLE
    }

    private fun showError(
        msg: String,
        btnText: String? = null,
        onClick: View.OnClickListener? = null
    ) {
        hideShimmer()
        if (isShowError) return
        isShowError = true
        binding.coinDetailError.root.visibility = View.VISIBLE
        binding.detailTabLayout.visibility = View.GONE
        binding.frmContainer.visibility = View.GONE
        binding.coinDetailError.tvErrorMessage.text = msg
        if (btnText?.isNotEmpty() == true) {
            binding.coinDetailError.btnErrorTryAgain.text = btnText
        } else {
            binding.coinDetailError.btnErrorTryAgain.text =
                ContextCompat.getString(this, R.string.try_again)
        }
        binding.coinDetailError.btnErrorTryAgain.setOnClickListener(onClick)
    }

    private fun hideError() {
        if (!isShowError) return
        isShowError = false
        binding.coinDetailError.root.visibility = View.GONE
        binding.detailTabLayout.visibility = View.VISIBLE
        binding.frmContainer.visibility = View.VISIBLE
    }

    private fun geBundle() {
        val stringJson = intent.extras?.getString(AppConstants.COIN_MARKET_DETAIL)
        if (stringJson == null) {
            isBackToPrevious = true
            showError(
                ContextCompat.getString(this, R.string.can_not_find_coin_data),
                btnText = "Back",
                onClick = this
            )
            return
        }
        val gson = Gson()
        args = gson.fromJson(stringJson, CoinDetailArgs::class.java)

        setToolbarMain(
            binding.detailToolbar,
            title = args.name,
            isShowBackButton = true,
        )
        binding.detailToolbar.toolbarMainIvFavorite.setOnClickListener(this)
        viewModel.fetchCoinDetail(args.id, isLoading = true)
        viewModel.checkWatchList(args.id)
    }


    private fun observeCoinDetail() {
        viewModel.coinDetailData.observe(this, Observer { res ->
            when (res) {
                is Resource.Loading -> {
                    showShimmer()
                }

                is Resource.Success -> {
                    hideShimmer()
                }

                is Resource.Error -> {
                    showError(
                        res.message ?: ContextCompat.getString(
                            this,
                            R.string.an_error_occurred
                        ), onClick = this
                    )
                }

            }
        })
    }

    private fun observeWatchList() {
        viewModel.watchListData.observe(this, Observer { res ->
            val favorite = binding.detailToolbar.toolbarMainIvFavorite
            favorite.visibility = View.VISIBLE
            if (res == null) {
                favorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_outline_24))
                return@Observer
            }
            favorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_24))
        })
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        when (tab?.position) {
            0 -> replaceFragment(
                PriceChartFragment.newInstance(),
                isAddToBackStack = false,
                anim = false
            )

            1 -> replaceFragment(
                ExchangesFragment.newInstance(),
                isAddToBackStack = false,
                anim = false
            )

            2 -> replaceFragment(
                InfoFragment.newInstance(),
                isAddToBackStack = false,
                anim = false
            )
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        // do nothing
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        // do nothing
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_error_try_again -> {
                if (isBackToPrevious) {
                    finish()
                    return
                }
                viewModel.fetchCoinDetail(args.id, isLoading = true)
            }
            R.id.toolbarMain_ivFavorite -> {
                viewModel.onWatchListChange()
            }
        }
    }
}