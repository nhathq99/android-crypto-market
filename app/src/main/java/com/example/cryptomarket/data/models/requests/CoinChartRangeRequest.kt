package com.example.cryptomarket.data.models.requests

import java.time.LocalDate
import java.util.Calendar
import java.util.Date

data class CoinChartRangeRequest(
    val vsCurrency: String = "usd",
    val from: Long,
    val to: Long,
) {
    val params
        get(): HashMap<String, Any> {
            return hashMapOf(
                "vs_currency" to vsCurrency,
                "from" to from,
                "to" to to,
            )
        }

    companion object {
        fun init(): CoinChartRangeRequest {
            val calendar = Calendar.getInstance()
            val now = calendar.timeInMillis / 1000
            calendar.add(Calendar.DATE, -1)
            val sub1day = calendar.timeInMillis / 1000
            return CoinChartRangeRequest(from = sub1day, to = now)
        }
    }
}