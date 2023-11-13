package com.example.cryptomarket.view.ui.coin_detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptomarket.R
import com.example.cryptomarket.bases.BaseFragment
import com.example.cryptomarket.data.models.Resource
import com.example.cryptomarket.data.models.responses.CoinDetailResponse
import com.example.cryptomarket.databinding.FragmentExchangesBinding
import com.example.cryptomarket.databinding.FragmentPriceChartBinding
import com.example.cryptomarket.view.adapters.ExchangeAdapter
import com.example.cryptomarket.view.adapters.PriceChartInfoAdapter
import com.example.cryptomarket.viewmodel.CoinDetailViewModel
import java.text.NumberFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ExchangesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExchangesFragment : BaseFragment() {

    private lateinit var binding: FragmentExchangesBinding
    private val viewModel: CoinDetailViewModel by activityViewModels()
    private val layoutManager = LinearLayoutManager(context)
    private val adapter = ExchangeAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentExchangesBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        val rv = binding.rvExchanges
        rv.layoutManager = layoutManager
        rv.adapter = adapter
        rv.setHasFixedSize(true)
        observeCoinDetail()
    }

    private fun observeCoinDetail() {
        viewModel.coinDetailData.observe(viewLifecycleOwner, Observer { res ->
            when (res) {
                is Resource.Loading -> {
                    setData(null)
                }

                is Resource.Success -> {
                    setData(res.data)
                }

                is Resource.Error -> {
                    setData(null)
                }

            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setData(data: CoinDetailResponse?) {
        if (data == null) {
            binding.root.visibility = View.INVISIBLE
            return
        }
        binding.root.visibility = View.VISIBLE
        adapter.setTickerList(data.coinDetail.tickers)
        adapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance(): ExchangesFragment {
            val args = Bundle()

            val fragment = ExchangesFragment()
            fragment.arguments = args
            return fragment
        }
    }
}