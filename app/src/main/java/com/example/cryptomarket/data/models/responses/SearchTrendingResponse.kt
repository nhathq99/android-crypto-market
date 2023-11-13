package com.example.cryptomarket.data.models.responses

import com.example.cryptomarket.data.models.search.SearchTrendingCoin
import com.google.gson.annotations.SerializedName

data class SearchTrendingResponse(
    @SerializedName("coins")
    val coins: MutableList<SearchTrendingCoin>
)