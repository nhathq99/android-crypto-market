package com.example.cryptomarket.data.network.api

import com.example.cryptomarket.data.models.CoinMarket
import com.example.cryptomarket.data.models.MarketChart
import com.example.cryptomarket.data.models.coin_detail.CoinDetail
import com.example.cryptomarket.data.models.responses.SearchResponse
import com.example.cryptomarket.data.models.responses.SearchTrendingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import java.util.*

interface CoinsApi {
    @GET("v3/coins/markets")
    suspend fun getCoinMarkets(@QueryMap params: HashMap<String, Any>): Response<List<CoinMarket>>
    @GET("v3/coins/{id}")
    suspend fun getCoinDetail(@Path("id") id: String, @QueryMap params: HashMap<String, Any>): Response<CoinDetail>
    @GET("v3/coins/{id}/market_chart/range")
    suspend fun getCoinChartRange(@Path("id") id: String, @QueryMap params: HashMap<String, Any>): Response<MarketChart>
    @GET("v3/search")
    suspend fun getSearch(@Query("query") query: String): Response<SearchResponse>
    @GET("v3/search/trending")
    suspend fun getSearchTrending(): Response<SearchTrendingResponse>
}