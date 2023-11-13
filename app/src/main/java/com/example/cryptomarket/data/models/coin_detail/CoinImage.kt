package com.example.cryptomarket.data.models.coin_detail


import com.google.gson.annotations.SerializedName

data class CoinImage(
    @SerializedName("large")
    val large: String,
    @SerializedName("small")
    val small: String,
    @SerializedName("thumb")
    val thumb: String
)