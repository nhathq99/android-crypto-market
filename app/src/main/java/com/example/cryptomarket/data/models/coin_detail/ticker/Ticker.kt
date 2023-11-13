package com.example.cryptomarket.data.models.coin_detail.ticker


import android.content.Context
import androidx.core.content.ContextCompat
import com.example.cryptomarket.R
import com.example.cryptomarket.data.models.coin_detail.Market
import com.google.gson.annotations.SerializedName

enum class TrustScore(val key: String, val color: Int) {
    Green("green", R.color.limeade),
    Yellow("yellow", R.color.super_nova),
    Red("red", R.color.grenadier),
    None("none", R.color.gray)
}


data class Ticker(
    @SerializedName("base")
    val base: String,
    @SerializedName("bid_ask_spread_percentage")
    val bidAskSpreadPercentage: Double,
    @SerializedName("coin_id")
    val coinId: String,
    @SerializedName("converted_last")
    val convertedLast: ConvertedLast,
    @SerializedName("converted_volume")
    val convertedVolume: ConvertedVolume,
    @SerializedName("is_anomaly")
    val isAnomaly: Boolean,
    @SerializedName("is_stale")
    val isStale: Boolean,
    @SerializedName("last")
    val last: Double,
    @SerializedName("last_fetch_at")
    val lastFetchAt: String,
    @SerializedName("last_traded_at")
    val lastTradedAt: String,
    @SerializedName("market")
    val market: Market,
    @SerializedName("target")
    val target: String,
    @SerializedName("target_coin_id")
    val targetCoinId: String,
    @SerializedName("timestamp")
    val timestamp: String,
    @SerializedName("token_info_url")
    val tokenInfoUrl: Any,
    @SerializedName("trade_url")
    val tradeUrl: String,
    @SerializedName("trust_score")
    val trustScore: String,
    @SerializedName("volume")
    val volume: Double
) {
    val trustScoreColor: Int get() {
        return when(trustScore) {
            TrustScore.Green.key -> TrustScore.Green.color

            TrustScore.Yellow.key -> TrustScore.Yellow.color

            TrustScore.Red.key -> TrustScore.Red.color

            else -> TrustScore.None.color
        }
    }
}