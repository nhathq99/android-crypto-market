package com.example.cryptomarket.data.models.coin_detail.coin_market_data

import com.google.gson.annotations.SerializedName

data class CoinMarketData (
    @SerializedName("current_price")
    val currentPrice: LocalizeDouble,
    @SerializedName("ath")
    val ath: LocalizeDouble,
    @SerializedName("ath_change_percentage")
    val athChangePercentage: LocalizeDouble,
    @SerializedName("ath_date")
    val athDate: LocalizeString,
    @SerializedName("atl")
    val atl: LocalizeDouble,
    @SerializedName("atl_change_percentage")
    val atlChangePercentage: LocalizeDouble,
    @SerializedName("atl_date")
    val atlDate: LocalizeString,
    @SerializedName("market_cap")
    val marketCap: LocalizeDouble,
    @SerializedName("fully_diluted_valuation")
    val fullyDilutedValuation: LocalizeDouble,
    @SerializedName("market_rank")
    val marketRank: Int,
    @SerializedName("market_cap_fdv_ratio")
    val marketCapFdvRatio: Double,
    @SerializedName("total_volume")
    val totalVolume: LocalizeDouble,
    @SerializedName("high_24h")
    val high24h: LocalizeDouble,
    @SerializedName("low_24h")
    val low24h: LocalizeDouble,
    @SerializedName("market_cap_change_24h")
    val marketCapChange24h: Double,
    @SerializedName("market_cap_change_percentage_24h")
    val marketCapChangePercentage24h: Double,
    @SerializedName("price_change_24h")
    val priceChange24h: Double,
    @SerializedName("price_change_percentage_14d")
    val priceChangePercentage14d: Double,
    @SerializedName("price_change_percentage_1y")
    val priceChangePercentage1y: Double,
    @SerializedName("price_change_percentage_200d")
    val priceChangePercentage200d: Double,
    @SerializedName("price_change_percentage_24h")
    val priceChangePercentage24h: Double,
    @SerializedName("price_change_percentage_30d")
    val priceChangePercentage30d: Double,
    @SerializedName("price_change_percentage_60d")
    val priceChangePercentage60d: Double,
    @SerializedName("price_change_percentage_7d")
    val priceChangePercentage7d: Double,
    @SerializedName("total_supply")
    val totalSupply: Double,
    @SerializedName("max_supply")
    val maxSupply: Double,
    @SerializedName("circulating_supply")
    val circulatingSupply: Double,
    @SerializedName("last_updated")
    val lastUpdated: String,
)
