package com.example.cryptomarket.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptomarket.data.models.local.CoinInfoItem
import com.example.cryptomarket.databinding.ItemOneLine3Binding
import com.example.cryptomarket.databinding.ItemOneLineBinding
import com.example.cryptomarket.databinding.ItemTextViewLinkBinding

class InfoAdapter: RecyclerView.Adapter<BaseViewHolder>() {
    private var items = mutableListOf<CoinInfoItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (viewType == CoinInfoItem.TYPE_NORMAL) {
            return NormalViewHolder(ItemOneLineBinding.inflate(LayoutInflater.from(parent.context),parent, false))
        }
        return LinksViewHolder(ItemOneLine3Binding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        if (items[position] is CoinInfoItem.NormalValue) {
            return CoinInfoItem.TYPE_NORMAL
        }
        return CoinInfoItem.TYPE_LINKS
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    fun setCoinInfos(infos: MutableList<CoinInfoItem>) {
        items = infos
    }

    inner class NormalViewHolder(val binding: ItemOneLineBinding): BaseViewHolder(binding.root) {
        override fun clear() {
            TODO("Not yet implemented")
        }

        override fun onBind(pos: Int) {
            super.onBind(pos)
            val value = items[pos] as CoinInfoItem.NormalValue
            binding.tvLabel.text = value.key
            binding.tvValue.text = value.value
        }
    }

    inner class LinksViewHolder(val binding: ItemOneLine3Binding): BaseViewHolder(binding.root) {
        override fun clear() {
            TODO("Not yet implemented")
        }

        override fun onBind(pos: Int) {
            super.onBind(pos)
            val link = items[pos] as CoinInfoItem.LinkValue
            binding.tvLabel.text = link.key
            val lnlLinks = binding.lnlLinks
            for (item in link.links) {
                val linkBinding = ItemTextViewLinkBinding.inflate(LayoutInflater.from(binding.root.context), binding.root, false)
                linkBinding.tvValue.text = item
                lnlLinks.addView(linkBinding.root)
            }
        }
    }
}