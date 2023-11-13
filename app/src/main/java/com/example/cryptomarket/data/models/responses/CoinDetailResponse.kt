package com.example.cryptomarket.data.models.responses

import com.example.cryptomarket.data.models.MarketChart
import com.example.cryptomarket.data.models.coin_detail.CoinDetail

data class CoinDetailResponse(
    val coinDetail: CoinDetail,
    val coinChart: MarketChart,
)
