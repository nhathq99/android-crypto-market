package com.example.cryptomarket.data.models.local

import com.google.gson.annotations.SerializedName

data class SearchCoinHistory(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("thumb")
    val thumb: String
)