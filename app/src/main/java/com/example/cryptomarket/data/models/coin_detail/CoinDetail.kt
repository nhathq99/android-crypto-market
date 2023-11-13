package com.example.cryptomarket.data.models.coin_detail

import android.util.Log
import com.example.cryptomarket.data.models.coin_detail.coin_link.CoinLink
import com.example.cryptomarket.data.models.coin_detail.coin_market_data.CoinMarketData
import com.example.cryptomarket.data.models.coin_detail.ticker.Ticker
import com.example.cryptomarket.data.models.local.CoinInfoItem
import com.example.cryptomarket.data.models.local.PriceChartInfoItem
import com.example.cryptomarket.utils.AppUtils
import com.google.gson.annotations.SerializedName
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

data class CoinDetail (
    @SerializedName("id")
    val id:String,
    @SerializedName("symbol")
    val symbol : String,
    @SerializedName("name")
    val name : String,
    @SerializedName("categories")
    val category : MutableList<String>,
    @SerializedName("description")
    val description : CoinDescription,
    @SerializedName("links")
    val links : CoinLink,
    @SerializedName("image")
    val image: CoinImage,
    @SerializedName("market_data")
    val marketData : CoinMarketData,
    @SerializedName("coingecko_rank")
    val coingeckoRank: Int,
    @SerializedName("coingecko_score")
    val coingeckoScore: Double,
    @SerializedName("community_score")
    val communityScore: Double,
    @SerializedName("developer_score")
    val developerScore: Double,
    @SerializedName("genesis_date")
    val genesisDate: String?,
    @SerializedName("liquidity_score")
    val liquidityScore: Double,
    @SerializedName("market_cap_rank")
    val marketCapRank: Int,
    @SerializedName("public_interest_score")
    val publicInterestScore: Double,
    @SerializedName("sentiment_votes_down_percentage")
    val sentimentVotesDownPercentage: Double,
    @SerializedName("sentiment_votes_up_percentage")
    val sentimentVotesUpPercentage: Double,
    @SerializedName("watchlist_portfolio_users")
    val watchlistPortfolioUsers: Int,
    @SerializedName("last_updated")
    val lastUpdated: String,
    @SerializedName("tickers")
    val tickers : MutableList<Ticker>,
    ) {
    val coinMarketInfos: MutableList<PriceChartInfoItem> get() {
        return mutableListOf(
            PriceChartInfoItem.TypeSingleValue("Market Cap Rank", "#${marketCapRank}"),
            PriceChartInfoItem.TypeSingleValue("Market Cap", NumberFormat.getCurrencyInstance().format(marketData.marketCap.usd)),
            PriceChartInfoItem.TypeSingleValue("Fully Diluted Valuation", NumberFormat.getCurrencyInstance().format(marketData.fullyDilutedValuation.usd)),
            PriceChartInfoItem.TypeSingleValue("Market Cap / FDV Ratio", marketData.marketCapFdvRatio.toString()),
            PriceChartInfoItem.TypeSingleValue("Trading Volume", NumberFormat.getCurrencyInstance().format(marketData.totalVolume.usd)),
            PriceChartInfoItem.TypeSingleValue("24H High", NumberFormat.getCurrencyInstance().format(marketData.high24h.usd)),
            PriceChartInfoItem.TypeSingleValue("24H Low", NumberFormat.getCurrencyInstance().format(marketData.low24h.usd)),
            PriceChartInfoItem.TypeSingleValue("Available Supply", AppUtils.humanReadable(marketData.circulatingSupply.toLong())),
            PriceChartInfoItem.TypeSingleValue("Total Supply", AppUtils.humanReadable(marketData.totalSupply.toLong())),
            PriceChartInfoItem.TypeSingleValue("Max Supply", AppUtils.humanReadable(marketData.maxSupply.toLong())),
            PriceChartInfoItem.TypeDateWithPriceValue("All-Time High", marketData.ath.usd, marketData.athChangePercentage.usd, marketData.athDate.usd),
            PriceChartInfoItem.TypeDateWithPriceValue("All-Time Low", marketData.atl.usd, marketData.atlChangePercentage.usd, marketData.atlDate.usd),
        )
    }

    val coinInfos: MutableList<CoinInfoItem> get() {
        Log.e("genesisDate", "genesisDate: $genesisDate", )
        return mutableListOf(
            CoinInfoItem.LinkValue("Homepage", links.homepage.filterNot { it.isEmpty() }.toMutableList()),
            CoinInfoItem.LinkValue("Blockchain/Supply", links.blockchainSite.filterNot { it.isEmpty() }.toMutableList()),
            CoinInfoItem.LinkValue("Discussion Forum", links.officialForumUrl.filterNot { it.isEmpty() }.toMutableList()),
            CoinInfoItem.NormalValue("Genesis Date", if (genesisDate?.isNotEmpty() == true) SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(genesisDate)) else "-"),
            CoinInfoItem.LinkValue("Twitter", mutableListOf("www.twitter.com/${links.twitterScreenName}")),
            CoinInfoItem.LinkValue("Facebook", mutableListOf("www.facebook.com/${links.facebookUsername}"))

        )
    }
}