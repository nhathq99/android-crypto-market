package com.example.cryptomarket.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.cryptomarket.databinding.ItemTextViewLinkBinding

class LinkAdapter(val context: Context, val links: MutableList<String>): BaseAdapter() {

    override fun getCount(): Int {
        return links.size
    }

    override fun getItem(p0: Int): Any {
        return links[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view: View
        val vh: ViewHolder
        if (p1 == null) {
            val binding = ItemTextViewLinkBinding.inflate(LayoutInflater.from(context), p2, false)
            vh = ViewHolder(binding)
            view = binding.root
            view.tag = vh
        } else {
            view = p1
            vh = view.tag as ViewHolder
        }
        vh.onBind(p0)
        return view
    }

    private inner class ViewHolder(val binding: ItemTextViewLinkBinding) {

        fun onBind(pos: Int) {
            binding.tvValue.text = links[pos]
        }
    }
}