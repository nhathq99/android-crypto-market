package com.example.cryptomarket.view.ui.coin_detail

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptomarket.R
import com.example.cryptomarket.bases.BaseFragment
import com.example.cryptomarket.data.models.MarketChart
import com.example.cryptomarket.data.models.Resource
import com.example.cryptomarket.data.models.coin_detail.coin_market_data.CoinMarketData
import com.example.cryptomarket.data.models.responses.CoinDetailResponse
import com.example.cryptomarket.databinding.FragmentPriceChartBinding
import com.example.cryptomarket.utils.AppUtils
import com.example.cryptomarket.utils.MpChartXAxisFormatter
import com.example.cryptomarket.view.adapters.PriceChartInfoAdapter
import com.example.cryptomarket.viewmodel.CoinDetailViewModel
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.Utils
import java.text.NumberFormat


/**
 * A simple [Fragment] subclass.
 * Use the [PriceChartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PriceChartFragment : BaseFragment() {

    private lateinit var binding: FragmentPriceChartBinding
    private val viewModel: CoinDetailViewModel by activityViewModels()
    private val layoutManager = object: LinearLayoutManager(context) {
        override fun canScrollVertically() = false
    }
    private val adapter = PriceChartInfoAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPriceChartBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        val rv = binding.priceInfo.rvInfo
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
        binding.tvCurrentPrice.text =
            NumberFormat.getCurrencyInstance().format(data.coinDetail.marketData.currentPrice.usd)
        setupChart(data.coinChart)
        setPricePercentChanges(data.coinDetail.marketData)
        adapter.setItemList(data.coinDetail.coinMarketInfos)
        adapter.notifyDataSetChanged()
    }

    private fun setupChart(coinChart: MarketChart) {
        val chart = binding.lineChart
        // background color
        chart.setBackgroundResource(R.drawable.content_bg)
        chart.description.isEnabled = false
        chart.setTouchEnabled(true)
        chart.setDrawGridBackground(false)
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        // chart.setScaleXEnabled(true);
        // chart.setScaleYEnabled(true);

        // force pinch zoom along both axis
        chart.setPinchZoom(true)
        chart.axisRight.isEnabled = false
        chart.axisLeft.textColor = ContextCompat.getColor(requireContext(), R.color.white)
        val xAxis = chart.xAxis
        xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.white)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = MpChartXAxisFormatter()
        xAxis.setDrawGridLines(false)
        setData(coinChart)

        // draw points over time

        // draw points over time
        chart.animateX(1500)

        // get the legend (only possible after setting data)

        // get the legend (only possible after setting data)
        val l: Legend = chart.legend

        // draw legend entries as lines

        // draw legend entries as lines
        l.form = LegendForm.LINE
    }

private fun setData(marketChart: MarketChart) {
    val chart = binding.lineChart
    val values = ArrayList<Entry>()
    val prices = marketChart.prices
    for (index in 0 until prices.size step 36) {
        values.add(Entry(prices[index][0].toFloat(), prices[index][1].toFloat()))
    }
    values.add(Entry(prices.last()[0].toFloat(), prices.last()[1].toFloat()))
    val set1: LineDataSet
    if (chart.data != null &&
        chart.data.dataSetCount > 0
    ) {
        set1 = chart.data!!.dataSets[0] as LineDataSet
        set1.values = values
        set1.notifyDataSetChanged()
        chart.data.notifyDataChanged()
        chart.notifyDataSetChanged()
    } else {
        // create a dataset and give it a type
        set1 = LineDataSet(values, "")
        set1.setDrawIcons(false)

        // text size of values
        set1.setDrawValues(false)
        set1.valueTextSize = 9f
        set1.valueTextColor = ContextCompat.getColor(requireContext(), R.color.white)
        set1.color = ContextCompat.getColor(requireContext(), R.color.cornflower_blue)
        set1.mode = LineDataSet.Mode.CUBIC_BEZIER
        // line thickness and point size
        set1.lineWidth = 4f
        set1.setDrawCircles(false)

        // draw points as solid circles
        set1.setDrawCircleHole(false)

        // customize legend entry
        set1.formLineWidth = 1f
        set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
        set1.formSize = 15f

        // draw selection line as dashed
        set1.enableDashedHighlightLine(10f, 5f, 0f)

        // set the filled area
        set1.setDrawFilled(true)
        set1.fillFormatter =
            IFillFormatter { _, _ -> chart.axisLeft.axisMinimum }

        // set color of filled area
        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.fade_primary)
            set1.fillDrawable = drawable
        } else {
            set1.fillColor = Color.BLACK
        }
        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1) // add the data sets

        // create a data object with the data sets
        val data = LineData(dataSets)

        // set data
        chart.data = data
    }
}



    private fun setPricePercentChanges(marketData: CoinMarketData) {
        val priceChangeBinding = binding.priceChange
        bindingPricePercent(
            marketData.priceChangePercentage24h,
            priceChangeBinding.iv24hPercent,
            priceChangeBinding.tv24hPercent
        )
        bindingPricePercent(
            marketData.priceChangePercentage7d,
            priceChangeBinding.iv7dPercent,
            priceChangeBinding.tv7dPercent
        )
        bindingPricePercent(
            marketData.priceChangePercentage14d,
            priceChangeBinding.iv14dPercent,
            priceChangeBinding.tv14dPercent
        )
        bindingPricePercent(
            marketData.priceChangePercentage30d,
            priceChangeBinding.iv30dPercent,
            priceChangeBinding.tv30dPercent
        )
        bindingPricePercent(
            marketData.priceChangePercentage60d,
            priceChangeBinding.iv60dPercent,
            priceChangeBinding.tv60dPercent
        )
        bindingPricePercent(
            marketData.priceChangePercentage1y,
            priceChangeBinding.iv1yPercent,
            priceChangeBinding.tv1yPercent
        )

    }

    private fun bindingPricePercent(value: Double, img: ImageView, tv: TextView) {
        tv.text = AppUtils.percentFormat(value)
        if (value < 0) {
            img.setImageResource(R.drawable.ic_arrow_down_24)
            img.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.grenadier))
            tv.setTextColor(ContextCompat.getColor(binding.root.context, R.color.grenadier))
            return
        }
        img.setImageResource(R.drawable.ic_arrow_up_24)
        img.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.limeade))
        tv.setTextColor(ContextCompat.getColor(binding.root.context, R.color.limeade))
    }

    companion object {
        fun newInstance(): PriceChartFragment {
            val args = Bundle()

            val fragment = PriceChartFragment()
            fragment.arguments = args
            return fragment
        }
    }
}