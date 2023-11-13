package com.example.cryptomarket.data.models.responses

import com.example.cryptomarket.data.models.search.SearchCoin
import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("coins")
    val coins: MutableList<SearchCoin>
)