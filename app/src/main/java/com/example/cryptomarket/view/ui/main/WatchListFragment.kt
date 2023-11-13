package com.example.cryptomarket.view.ui.main

import android.content.Intent
import android.os.Bundle
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
import com.example.cryptomarket.data.models.local.CoinWatchList
import com.example.cryptomarket.databinding.FragmentWatchListBinding
import com.example.cryptomarket.view.adapters.CoinWatchListAdapter
import com.example.cryptomarket.view.customs.ActionClickListener
import com.example.cryptomarket.view.customs.RecyclerViewSwipeHelper
import com.example.cryptomarket.view.interfaces.ItemClickListener
import com.example.cryptomarket.view.ui.coin_detail.CoinDetailActivity
import com.example.cryptomarket.viewmodel.WatchListViewModel
import com.google.gson.Gson

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class WatchListFragment : BaseFragment() {

    companion object {
        fun newInstance(): WatchListFragment {
            val args = Bundle()

            val fragment = WatchListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var _binding: FragmentWatchListBinding? = null
    private val viewModel: WatchListViewModel by viewModels()
    private val layoutManager = LinearLayoutManager(context)
    private val adapter = CoinWatchListAdapter()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWatchListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun init() {
        setToolbarMain(
            binding.watchListToolbar,
            title = ContextCompat.getString(requireContext(), R.string.watch_list),
        )
        val rv = binding.rvWatchList
        rv.layoutManager = layoutManager
        rv.adapter = adapter
        rv.setHasFixedSize(true)
        listenWatchListItemClicked()
        watchListRecyclerViewSwipeAction()
        observeWatchList()
    }

    private fun observeWatchList() {
        viewModel.coinWatchListData.observe(viewLifecycleOwner, Observer { res ->
            when (res) {
                is Resource.Loading -> {
                    displayError(false)
                    displayWatchList(false)
                    displayLoading(true)
                }

                is Resource.Success -> {
                    displayLoading(false)
                    displayWatchList(true, res.data ?: mutableListOf())
                }

                is Resource.Error -> {
                    displayLoading(false)
                    displayError(true, res.message) {
                        viewModel.fetchCoinWatchList()
                    }
                }

            }
        })
    }

    private fun displayLoading(showing: Boolean) {
        if (showing) {
            binding.watchListShimmer.root.visibility = View.VISIBLE
            binding.watchListShimmer.shimmerLayout.startShimmerAnimation()
            return
        }
        binding.watchListShimmer.root.visibility = View.GONE
        binding.watchListShimmer.shimmerLayout.stopShimmerAnimation()
    }

    private fun displayError(showing: Boolean, msg: String? = null, onClickListener: OnClickListener? = null) {
        if (showing) {
            binding.layoutError.root.visibility = View.VISIBLE
            binding.layoutError.tvErrorMessage.text =
                msg ?: ContextCompat.getString(requireContext(), R.string.an_error_occurred)
            binding.layoutError.btnErrorTryAgain.text =
                ContextCompat.getString(requireContext(), R.string.try_again)
            binding.layoutError.btnErrorTryAgain.setOnClickListener(onClickListener)
            return
        }
        binding.layoutError.root.visibility = View.GONE
    }

    private fun  displayWatchList(showing: Boolean, coins: MutableList<CoinWatchList>? = null) {
        if (showing) {
            if (coins ==null) return
            if (coins.isEmpty()) {
                binding.layoutNothing.root.visibility = View.VISIBLE
                binding.rvWatchList.visibility = View.GONE
                return
            }
            binding.layoutNothing.root.visibility = View.GONE
            binding.rvWatchList.visibility = View.VISIBLE
            adapter.setCoinWatchList(coins)
            adapter.notifyDataSetChanged()
            return
        }
        binding.rvWatchList.visibility = View.GONE
        binding.layoutNothing.root.visibility = View.GONE
    }

    private fun listenWatchListItemClicked() {
        adapter.setOnItemClickListener(object : ItemClickListener {
            override fun onClick(position: Int, item: Any) {
                val coin = item as CoinWatchList
                val gson = Gson()
                val args = CoinDetailArgs(coin.id, coin.thumb,coin.symbol, coin.name)
                val stringJson = gson.toJson(args)
                val intent = Intent(requireContext(), CoinDetailActivity::class.java)
                intent.putExtra(AppConstants.COIN_MARKET_DETAIL, stringJson)
                startActivity(intent)
            }
        })
    }

    private fun watchListRecyclerViewSwipeAction() {
        object: RecyclerViewSwipeHelper(requireContext(), binding.rvWatchList, 200) {
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
                            viewModel.removeWatchListItemByPosition(pos)
                        }

                    }
                ))
            }

        }
    }

    override fun onResume() {
        super.onResume()
        if (isVisible) {
            viewModel.fetchCoinWatchList()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}