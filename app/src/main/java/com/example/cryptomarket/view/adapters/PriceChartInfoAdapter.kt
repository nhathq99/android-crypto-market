package com.example.cryptomarket.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptomarket.R
import com.example.cryptomarket.data.models.local.PriceChartInfoItem
import com.example.cryptomarket.databinding.ItemOneLine2Binding
import com.example.cryptomarket.databinding.ItemOneLineBinding
import com.example.cryptomarket.utils.AppUtils
import java.text.DateFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class PriceChartInfoAdapter: RecyclerView.Adapter<BaseViewHolder>() {
    private var items = mutableListOf<PriceChartInfoItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (viewType == PriceChartInfoItem.TYPE_DATE_WITH_PRICE_VALUE) {
            val binding = ItemOneLine2Binding.inflate(LayoutInflater.from(parent.context),parent, false)
            return DateWithPriceViewHolder(binding)
        }
        val binding = ItemOneLineBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return SingleValueViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemViewType(position: Int): Int {
        if (items[position] is PriceChartInfoItem.TypeDateWithPriceValue) {
            return PriceChartInfoItem.TYPE_DATE_WITH_PRICE_VALUE
        }
        return PriceChartInfoItem.TYPE_SINGLE_VALUE
    }

    fun setItemList(items: MutableList<PriceChartInfoItem>) {
        this.items = items
    }

    inner class SingleValueViewHolder(private val binding: ItemOneLineBinding): BaseViewHolder(binding.root) {
        override fun clear() {
            TODO("Not yet implemented")
        }

        override fun onBind(pos: Int) {
            super.onBind(pos)
            val data = items[pos] as PriceChartInfoItem.TypeSingleValue
            binding.tvLabel.text = data.key
            binding.tvValue.text = data.value
        }
    }

    inner class DateWithPriceViewHolder(private val binding: ItemOneLine2Binding): BaseViewHolder(binding.root) {
        override fun clear() {
            TODO("Not yet implemented")
        }

        @SuppressLint("SetTextI18n")
        override fun onBind(pos: Int) {
            super.onBind(pos)
            val data = items[pos] as PriceChartInfoItem.TypeDateWithPriceValue
            val orgFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val formatter = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
            val date = orgFormatter.parse(data.time)
            val ms = Calendar.getInstance().timeInMillis - date.time
            val days = TimeUnit.MILLISECONDS.toDays(ms)
            binding.tvLabel.text = data.key
            binding.tvValue.text = NumberFormat.getCurrencyInstance().format(data.price)
            binding.tvDate.text = "${formatter.format(date)} (${days} days)"
            binding.tvPercentChange.text = AppUtils.percentFormat(data.pricePercent)
            if (data.pricePercent > 0) {
                binding.ivUpDown.setImageResource(R.drawable.ic_arrow_up_24)
                binding.tvPercentChange.setTextColor(ContextCompat.getColor(binding.root.context, R.color.limeade))
                binding.ivUpDown.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.limeade))
            } else {
                binding.ivUpDown.setImageResource(R.drawable.ic_arrow_down_24)
                binding.tvPercentChange.setTextColor(ContextCompat.getColor(binding.root.context, R.color.grenadier))
                binding.ivUpDown.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.grenadier))
            }
        }
    }
}