package com.example.cryptomarket.data.models.local

import com.google.gson.annotations.SerializedName

data class CoinDetailArgs(
    @SerializedName("id")
    val id: String,
    @SerializedName("logo")
    val logo: String,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("name")
    val name: String,
)