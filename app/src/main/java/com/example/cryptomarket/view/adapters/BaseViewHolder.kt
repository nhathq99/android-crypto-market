package com.example.cryptomarket.view.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private var currentPos = 0

    protected abstract fun clear()

    open fun onBind(pos: Int) {
       currentPos = pos
    }

    open fun getCurrentPosition(): Int {
        return currentPos;
    }
}