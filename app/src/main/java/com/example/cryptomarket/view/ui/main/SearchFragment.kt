package com.example.cryptomarket.view.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptomarket.R
import com.example.cryptomarket.bases.BaseFragment
import com.example.cryptomarket.constants.AppConstants
import com.example.cryptomarket.data.models.Resource
import com.example.cryptomarket.data.models.local.CoinDetailArgs
import com.example.cryptomarket.data.models.local.SearchCoinHistory
import com.example.cryptomarket.data.models.responses.SearchResponse
import com.example.cryptomarket.data.models.responses.SearchTrendingResponse
import com.example.cryptomarket.data.models.search.Item
import com.example.cryptomarket.data.models.search.SearchCoin
import com.example.cryptomarket.data.models.search.SearchTrendingCoin
import com.example.cryptomarket.databinding.FragmentSearchBinding
import com.example.cryptomarket.view.adapters.SearchCoinAdapter
import com.example.cryptomarket.view.adapters.SearchCoinHistoryAdapter
import com.example.cryptomarket.view.adapters.SearchTrendingCoinAdapter
import com.example.cryptomarket.view.customs.ActionClickListener
import com.example.cryptomarket.view.customs.RecyclerViewSwipeHelper
import com.example.cryptomarket.view.interfaces.ItemClickListener
import com.example.cryptomarket.view.ui.coin_detail.CoinDetailActivity
import com.example.cryptomarket.viewmodel.SearchViewModel
import com.google.gson.Gson
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SearchFragment : BaseFragment() {

    companion object {
        fun newInstance(): SearchFragment {
            val args = Bundle()

            val fragment = SearchFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var _binding: FragmentSearchBinding? = null
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var edtSearchSub: Disposable
    private val searchCoinAdapter = SearchCoinAdapter()
    private val searchCoinHistoryAdapter = SearchCoinHistoryAdapter()
    private val searchTrendingCoinAdapter = SearchTrendingCoinAdapter()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        val rvSearchTrendingCoin = binding.rvTrendingSearch
        val rvSearchHistory = binding.rvSearchHistory
        val rvSearchCoin = binding.rvSearching
        // set layout manager
        rvSearchTrendingCoin.layoutManager = object : LinearLayoutManager(context) {
            override fun canScrollVertically() = false
        }
        rvSearchHistory.layoutManager = object : LinearLayoutManager(context) {
            override fun canScrollVertically() = false
        }
        rvSearchCoin.layoutManager = object : LinearLayoutManager(context) {
            override fun canScrollVertically() = false
        }
        // set adapter
        rvSearchTrendingCoin.adapter = searchTrendingCoinAdapter
        rvSearchHistory.adapter = searchCoinHistoryAdapter
        rvSearchCoin.adapter = searchCoinAdapter
        // set has fixed size
        rvSearchTrendingCoin.setHasFixedSize(true)
        rvSearchHistory.setHasFixedSize(true)
        rvSearchCoin.setHasFixedSize(true)
        //listen item list clicked
        listenSearchCoinClicked()
        listenSearchCoinHistoryClicked()
        listenSearchCoinHistoryClearAllClicked()
        listenSearchTrendingCoinClicked()
        historyRecyclerViewSwipeAction()

        //listen edt search change
        edtSearchSub =
            binding.edtSearch.textChanges().debounce(800, TimeUnit.MILLISECONDS).subscribe {
                if (it.toString().isEmpty()) {
                    val data = viewModel.searchHistoryAndTrending ?: return@subscribe
                    activity?.runOnUiThread {
                        displaySearchHistoryAndTrendingResult(true, data)
                        displaySearchResult(false)
                    }
                    return@subscribe
                }
                viewModel.fetchSearch(it.toString())
            }

        observeSearchHistoryAndTrending()
        observeSearchCoins()
        viewModel.fetchSearchHistoryAndTrending()
    }

    private fun observeSearchHistoryAndTrending() {
        viewModel.searchHistoryAndTrendingData.observe(viewLifecycleOwner, Observer { res ->
            when (res) {
                is Resource.Loading -> {
                    displayError(false)
                    displaySearchResult(false)
                    displaySearchHistoryAndTrendingResult(false)
                    displayLoading(true)
                }

                is Resource.Success -> {
                    displayLoading(false)
                    displaySearchHistoryAndTrendingResult(true, res.data)
                }

                is Resource.Error -> {
                    displayLoading(false)
                    displayError(true, res.message) {
                        viewModel.fetchSearchHistoryAndTrending()
                    }
                }

            }
        })
    }

    private fun observeSearchCoins() {
        viewModel.searchData.observe(viewLifecycleOwner, Observer { res ->
            when (res) {
                is Resource.Loading -> {
                    displayError(false)
                    displaySearchResult(false)
                    displaySearchHistoryAndTrendingResult(false)
                    displayLoading(true)
                }

                is Resource.Success -> {
                    displayLoading(false)
                    displaySearchResult(true, res.data)
                }

                is Resource.Error -> {
                    displayLoading(false)
                    displayError(true, res.message) {
                        viewModel.fetchSearch(binding.edtSearch.text.toString())
                    }
                }

            }
        })
    }

    private fun displayLoading(showing: Boolean) {
        if (showing) {
            binding.shmLoading.root.visibility = View.VISIBLE
            return
        }
        binding.shmLoading.root.visibility = View.GONE
    }

    private fun displayError(
        showing: Boolean,
        msg: String? = null,
        onClickListener: OnClickListener? = null
    ) {
        if (showing) {
            binding.loError.root.visibility = View.VISIBLE
            binding.loError.tvErrorMessage.text =
                msg ?: ContextCompat.getString(requireContext(), R.string.an_error_occurred)
            binding.loError.btnErrorTryAgain.text =
                ContextCompat.getString(requireContext(), R.string.try_again)
            binding.loError.btnErrorTryAgain.setOnClickListener(onClickListener)
            return
        }
        binding.loError.root.visibility = View.GONE
    }

    private fun displaySearchResult(showing: Boolean, res: SearchResponse? = null) {
        if (showing) {
            binding.nsvSearching.visibility = View.VISIBLE
            if (res == null) {
                return
            }
            searchCoinAdapter.setSearchCoins(res.coins)
            searchCoinAdapter.notifyDataSetChanged()
            return
        }
        binding.nsvSearching.visibility = View.GONE
    }

    private fun displaySearchHistoryAndTrendingResult(
        showing: Boolean,
        data: Pair<MutableList<SearchCoinHistory>, SearchTrendingResponse>? = null
    ) {
        if (showing) {
            binding.nsvHistoryTrending.visibility = View.VISIBLE
            if (data == null) return

            if (data.first.isEmpty()) {
                binding.llHistoryHeader.visibility = View.GONE
                binding.frmSearchHistory.visibility = View.GONE
            } else {
                searchCoinHistoryAdapter.setSearchCoinHistories(data.first)
                searchCoinHistoryAdapter.notifyDataSetChanged()
                binding.llHistoryHeader.visibility = View.VISIBLE
                binding.frmSearchHistory.visibility = View.VISIBLE
            }

            searchTrendingCoinAdapter.setSearchCoins(data.second.coins)
            searchTrendingCoinAdapter.notifyDataSetChanged()
            return
        }
        binding.nsvHistoryTrending.visibility = View.GONE
    }

    private fun listenSearchCoinClicked() {
        searchCoinAdapter.setOnItemClickListener(object : ItemClickListener {
            override fun onClick(position: Int, item: Any) {
                val coin = item as SearchCoin
                viewModel.addHistorySearch(coin.history)
                val gson = Gson()
                val args = CoinDetailArgs(coin.id, coin.thumb,coin.symbol, coin.name)
                val stringJson = gson.toJson(args)
                val intent = Intent(requireContext(), CoinDetailActivity::class.java)
                intent.putExtra(AppConstants.COIN_MARKET_DETAIL, stringJson)
                startActivity(intent)
            }
        })
    }

    private fun listenSearchCoinHistoryClicked() {
        searchCoinHistoryAdapter.setOnItemClickListener(object : ItemClickListener {
            override fun onClick(position: Int, item: Any) {
                val coin = item as SearchCoinHistory
                val gson = Gson()
                val args = CoinDetailArgs(coin.id, coin.thumb,coin.symbol, coin.name)
                val stringJson = gson.toJson(args)
                val intent = Intent(requireContext(), CoinDetailActivity::class.java)
                intent.putExtra(AppConstants.COIN_MARKET_DETAIL, stringJson)
                startActivity(intent)
            }
        })
    }
    private fun listenSearchCoinHistoryClearAllClicked() {
        binding.tvHistoryClear.setOnClickListener {
            viewModel.clearAllSearchHistory()
        }
    }


    private fun listenSearchTrendingCoinClicked() {
        searchTrendingCoinAdapter.setOnItemClickListener(object : ItemClickListener {
            override fun onClick(position: Int, item: Any) {
                val coin = item as Item
                val gson = Gson()
                val args = CoinDetailArgs(coin.id, coin.thumb,coin.symbol, coin.name)
                val stringJson = gson.toJson(args)
                val intent = Intent(requireContext(), CoinDetailActivity::class.java)
                intent.putExtra(AppConstants.COIN_MARKET_DETAIL, stringJson)
                startActivity(intent)
            }
        })
    }

    private fun historyRecyclerViewSwipeAction() {
        object: RecyclerViewSwipeHelper(requireContext(), binding.rvSearchHistory, 200) {
            override fun instantiateMyButton(
                viewHolder: RecyclerView.ViewHolder?,
                buffer: MutableList<ActionButton>?
            ) {
                buffer?.add(ActionButton(
                    requireContext(),
                    ContextCompat.getString(requireContext(), R.string.delete),
                    35,
                    0,
                    ContextCompat.getColor(requireContext(), R.color.grenadier),
                    object: ActionClickListener {
                        override fun onClick(pos: Int) {
                            viewModel.removeHistoryByPosition(pos)
                        }

                    }
                ))
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        edtSearchSub.dispose()
        _binding = null
    }
}