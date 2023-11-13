package com.example.cryptomarket.data.models

import com.google.gson.annotations.SerializedName

data class MarketChart (
    @SerializedName("prices")
    val prices : MutableList<MutableList<Double>>,
    @SerializedName("market_caps")
    val marketCaps : MutableList<MutableList<Double>>,
    @SerializedName("total_volumes")
    val totalVolumes : MutableList<MutableList<Double>>,
)