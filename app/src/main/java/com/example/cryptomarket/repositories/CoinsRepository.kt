package com.example.cryptomarket.repositories

import com.example.cryptomarket.data.models.requests.CoinChartRangeRequest
import com.example.cryptomarket.data.models.requests.CoinDetailRequest
import com.example.cryptomarket.data.network.api.CoinsApi
import com.example.cryptomarket.data.models.requests.CoinMarketRequest
import javax.inject.Inject

class CoinsRepository @Inject constructor(private val coinsApi: CoinsApi){
    suspend fun getCoinMarketList(request: CoinMarketRequest) = coinsApi.getCoinMarkets(request.params)
    suspend fun getCoinDetail(id: String, request: CoinDetailRequest) = coinsApi.getCoinDetail(id, request.params)
    suspend fun getCoinChartRange(id: String, request: CoinChartRangeRequest) = coinsApi.getCoinChartRange(id, request.params)
    suspend fun getSearch(query: String) = coinsApi.getSearch(query)
    suspend fun getSearchTrending() = coinsApi.getSearchTrending()
}