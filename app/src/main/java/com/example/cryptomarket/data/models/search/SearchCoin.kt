package com.example.cryptomarket.data.models.search


import com.example.cryptomarket.data.models.local.SearchCoinHistory
import com.google.gson.annotations.SerializedName

data class SearchCoin(
    @SerializedName("api_symbol")
    val apiSymbol: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("large")
    val large: String,
    @SerializedName("market_cap_rank")
    val marketCapRank: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("thumb")
    val thumb: String
) {
    val history: SearchCoinHistory get() {
        return SearchCoinHistory(id, name, symbol, thumb)
    }
}