package com.example.cryptomarket.view.adapters

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptomarket.R
import com.example.cryptomarket.data.models.CoinMarket
import com.example.cryptomarket.data.models.coin_detail.ticker.Ticker
import com.example.cryptomarket.databinding.ItemExchangeBinding
import com.example.cryptomarket.utils.overrideColor
import java.text.NumberFormat

class ExchangeAdapter: RecyclerView.Adapter<BaseViewHolder>() {
    private var items = mutableListOf<Ticker>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolder(ItemExchangeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    fun setTickerList(tickers: MutableList<Ticker>) {
        items = tickers.toMutableList()
    }

    inner class ViewHolder(val binding: ItemExchangeBinding): BaseViewHolder(binding.root) {
        override fun clear() {
            TODO("Not yet implemented")
        }

        override fun onBind(pos: Int) {
            super.onBind(pos)
            val ticker = items[pos]
            binding.tvChainName.text = ticker.market.name
            binding.tvChainStatus.text = ticker.trustScore
            binding.tvPair1.text = ticker.base
            binding.tvPair2.text = ticker.target
            binding.tvPrice.text = NumberFormat.getCurrencyInstance().format(ticker.convertedLast.usd)
            binding.tv24hVol.text = NumberFormat.getCurrencyInstance().format(ticker.convertedVolume.usd)
            binding.tvChainStatus.background.overrideColor(ContextCompat.getColor(binding.root.context, ticker.trustScoreColor))

        }
    }
}