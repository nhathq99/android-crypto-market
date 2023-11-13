package com.example.cryptomarket.data.models.search


import com.google.gson.annotations.SerializedName

data class SearchTrendingCoin(
    @SerializedName("item")
    val item: Item
)