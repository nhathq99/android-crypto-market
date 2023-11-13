package com.example.cryptomarket.data.models.responses

import com.example.cryptomarket.data.models.CoinMarket

data class CoinMarketResponse (
    var coinMarkets: MutableList<CoinMarket>,
    var currentPage: Int = 1,
    var hasReachedMax: Boolean = false
)