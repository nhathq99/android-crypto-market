package com.example.cryptomarket.data.models.coin_detail


import com.google.gson.annotations.SerializedName

data class Market(
    @SerializedName("has_trading_incentive")
    val hasTradingIncentive: Boolean,
    @SerializedName("identifier")
    val identifier: String,
    @SerializedName("name")
    val name: String
)