package com.example.cryptomarket.view.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptomarket.R
import com.example.cryptomarket.databinding.ItemListCoinMarketBinding
import com.example.cryptomarket.data.models.CoinMarket
import com.example.cryptomarket.databinding.ItemLoadingBinding
import com.example.cryptomarket.utils.AppUtils
import com.example.cryptomarket.view.interfaces.ItemClickListener
import java.text.DecimalFormat
import java.text.NumberFormat

private const val VIEW_TYPE_ITEM = 0
private const val VIEW_TYPE_LOADING = 1

class CoinMarketAdapter: RecyclerView.Adapter<BaseViewHolder>() {
    private var coinMarketList = mutableListOf<CoinMarket?>()
    var isLoadMore = false
        get() = field
    private lateinit var itemClickListener: ItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (viewType == VIEW_TYPE_ITEM) {
            val binding = ItemListCoinMarketBinding.inflate(LayoutInflater.from(parent.context),parent, false)
            return ViewHolder(binding)
        }
        val binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return coinMarketList.size
    }

    override fun getItemViewType(position: Int): Int {
        if (coinMarketList.isNotEmpty() && coinMarketList.size - 1 == position && isLoadMore) {
            return VIEW_TYPE_LOADING
        }
        return VIEW_TYPE_ITEM
    }


    fun setCoinMarketList(coinMarkets: MutableList<CoinMarket>) {
        coinMarketList = coinMarkets.toMutableList()
    }

    fun setOnItemClick(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    fun showLoading() {
        isLoadMore = true
        coinMarketList.add(null)
        notifyItemInserted(coinMarketList.size - 1)
    }

    fun hideLoading() {
        isLoadMore = false
        if (coinMarketList.last() == null) {
            coinMarketList.removeLast()
            notifyItemRemoved(coinMarketList.size + 1)
        }
    }

    inner class ViewHolder(private val binding: ItemListCoinMarketBinding): BaseViewHolder(binding.root) {

        override fun clear() {
            TODO("Not yet implemented")
        }

        override fun onBind(pos: Int) {
            super.onBind(pos)
            val coin = coinMarketList[pos]!!
            Glide.with(binding.root).load(coin.image).centerCrop().into(binding.imgCryptoLogo)
            binding.tvCoinName.text = coin.name
            binding.tvMarketCap.text = AppUtils.humanReadable(coin.marketCap)
            binding.tvPrice.text = NumberFormat.getCurrencyInstance().format(coin.currentPrice)
            binding.tvPriceChange.text = AppUtils.percentFormat(coin.priceChangePercentage24h, minDigits = 1)
            if (coin.priceChangePercentage24h > 0) {
                binding.imgPriceChange.setImageResource(R.drawable.ic_arrow_up_24)
                binding.tvPriceChange.setTextColor(ContextCompat.getColor(binding.root.context, R.color.limeade))
                binding.imgPriceChange.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.limeade))
            } else {
                binding.imgPriceChange.setImageResource(R.drawable.ic_arrow_down_24)
                binding.tvPriceChange.setTextColor(ContextCompat.getColor(binding.root.context, R.color.grenadier))
                binding.imgPriceChange.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.grenadier))
            }
            binding.root.setOnClickListener { itemClickListener.onClick(pos, coin) }
        }

    }

    inner class LoadingViewHolder(private val binding: ItemLoadingBinding): BaseViewHolder(binding.root) {
        override fun clear() {
            TODO("Not yet implemented")
        }

        override fun onBind(pos: Int) {
            super.onBind(pos)
            binding.itemLoadingProgressBar.visibility = View.VISIBLE
        }
    }
}