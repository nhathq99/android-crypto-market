package com.example.cryptomarket.view.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.cryptomarket.R
import com.example.cryptomarket.bases.BaseApplication
import com.example.cryptomarket.bases.BaseFragment
import com.example.cryptomarket.constants.AppConstants
import com.example.cryptomarket.data.models.CoinMarket
import com.example.cryptomarket.data.models.Paging
import com.example.cryptomarket.data.models.Resource
import com.example.cryptomarket.data.models.local.CoinDetailArgs
import com.example.cryptomarket.data.models.requests.CoinMarketRequest
import com.example.cryptomarket.databinding.FragmentMarketBinding
import com.example.cryptomarket.utils.AppUtils
import com.example.cryptomarket.view.adapters.CoinMarketAdapter
import com.example.cryptomarket.view.interfaces.ItemClickListener
import com.example.cryptomarket.view.ui.coin_detail.CoinDetailActivity
import com.example.cryptomarket.viewmodel.DashboardViewModel
import com.google.gson.Gson


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MarketFragment : BaseFragment(), ItemClickListener {

    companion object {
        fun newInstance(): MarketFragment {
            val args = Bundle()

            val fragment = MarketFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var _binding: FragmentMarketBinding? = null
    private val layoutManager = LinearLayoutManager(context)
    private val adapter = CoinMarketAdapter()
    private val paging = Paging()
    private var isShowShimmer = false
    private var isScrolling = false
    private var currentItems = 0
    private var totalItems = 0
    private var scrollOutItems = 0
    private val viewModel: DashboardViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarketBinding.inflate(inflater, container, false)
        val toolbarBinding = binding.dashboardToolbar
        setToolbarMain(toolbarBinding, "Market")
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        adapter.setOnItemClick(this)
        val rv = binding.rvCoinMarket
        rv.layoutManager = layoutManager
        rv.adapter = adapter
        rv.setHasFixedSize(true)
        handleScroll()
        setViewModel()
    }

    private fun showShimmer() {
        if (isShowShimmer) return
        isShowShimmer = true
        binding.coinMarketShimmer.root.visibility = View.VISIBLE
        binding.coinMarketShimmer.shimmerLayout.startShimmerAnimation()
        binding.rvCoinMarket.visibility = View.GONE
    }

    private fun hideShimmer() {
        if (!isShowShimmer) return
        isShowShimmer = false
        binding.coinMarketShimmer.root.visibility = View.GONE
        binding.coinMarketShimmer.shimmerLayout.stopShimmerAnimation()
        binding.rvCoinMarket.visibility = View.VISIBLE
    }

    private fun setViewModel() {
        observeCoinMarkets()

        val request = CoinMarketRequest(
            page = paging.page,
            perPage = paging.perPage,
        )
        viewModel.fetchCoinMarkets(request, isLoading = true)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeCoinMarkets() {
        viewModel.coinMarketData.observe(viewLifecycleOwner, Observer { res ->
            when (res) {
                is Resource.Loading -> {
                    showShimmer()
                }

                is Resource.Success -> {
                    hideShimmer()
                    if (res.data != null) {
                        paging.page = res.data.currentPage;
                        paging.hasReachedMax = res.data.hasReachedMax;
                        adapter.setCoinMarketList(res.data.coinMarkets)
                        adapter.notifyDataSetChanged()
                        if (adapter.isLoadMore) {
                            adapter.hideLoading()
                        }
                    }
                }

                is Resource.Error -> {
                    hideShimmer()
                    showToast(
                        res.message ?: ContextCompat.getString(
                            this.requireContext(),
                            R.string.an_error_occurred
                        )
                    )
                    if (adapter.isLoadMore) {
                        adapter.hideLoading()
                    }
                }

            }
        })
    }

    private fun handleScroll() {
        binding.rvCoinMarket.addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL && adapter.itemCount == paging.perPage) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentItems = layoutManager.childCount
                totalItems = layoutManager.itemCount
                scrollOutItems = layoutManager.findFirstVisibleItemPosition()
                if (isScrolling && currentItems + scrollOutItems == totalItems && !paging.hasReachedMax && !adapter.isLoadMore) {
                    isScrolling = false
                    loadMoreCoinMarkets()
                }
            }
        })
    }

    fun loadMoreCoinMarkets() {
        if (!AppUtils.hasInternetConnection(requireActivity().application as BaseApplication)) {
            showToast(ContextCompat.getString(requireContext(), R.string.network_error))
            return
        }
        val request = CoinMarketRequest(
            page = paging.page + 1,
            perPage = paging.perPage,
        )
        viewModel.fetchCoinMarkets(request)
        adapter.showLoading()
    }

    fun refreshCoinMarkets() {
        val request = CoinMarketRequest(
            page = 1,
            perPage = paging.perPage,
        )
        viewModel.fetchCoinMarkets(request)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(position: Int, item: Any) {
        val coin = item as CoinMarket
        val gson = Gson()
        val args = CoinDetailArgs(coin.id, coin.image,coin.symbol, coin.name)
        val stringJson = gson.toJson(args)
        val intent = Intent(this.context, CoinDetailActivity::class.java)
        intent.putExtra(AppConstants.COIN_MARKET_DETAIL, stringJson)
        startActivity(intent)
    }

}