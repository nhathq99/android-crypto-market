package com.example.cryptomarket.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptomarket.data.models.local.SearchCoinHistory
import com.example.cryptomarket.data.models.search.SearchCoin
import com.example.cryptomarket.databinding.ItemSimpleCoinInfoBinding
import com.example.cryptomarket.view.interfaces.ItemClickListener
import java.lang.StringBuilder

class SearchCoinHistoryAdapter: RecyclerView.Adapter<BaseViewHolder>() {
    private var coins = mutableListOf<SearchCoinHistory>()
    private var itemClickListener: ItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolder(ItemSimpleCoinInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return coins.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    fun setSearchCoinHistories(coins: MutableList<SearchCoinHistory>) {
        this.coins = coins
    }

    fun setOnItemClickListener(listener: ItemClickListener) {
        itemClickListener = listener
    }

    inner class ViewHolder(val binding: ItemSimpleCoinInfoBinding): BaseViewHolder(binding.root) {
        override fun clear() {
            TODO("Not yet implemented")
        }

        override fun onBind(pos: Int) {
            super.onBind(pos)
            val coin = coins[pos]
            Glide.with(binding.root).load(coin.thumb).centerCrop().into(binding.imgCryptoLogo)
            binding.tvCoinName.text = coin.symbol
            binding.tvCoinFullName.text = coin.name
            binding.tvCoinRank.visibility = View.GONE
            binding.root.setOnClickListener {
                itemClickListener?.onClick(pos, coin)
            }
        }
    }
}