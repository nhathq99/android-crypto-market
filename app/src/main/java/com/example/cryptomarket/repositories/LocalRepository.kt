package com.example.cryptomarket.repositories

import android.content.Context
import com.example.cryptomarket.data.models.local.CoinWatchList
import com.example.cryptomarket.data.models.local.SearchCoinHistory
import com.example.cryptomarket.utils.SharePrefUtils
import javax.inject.Inject

class LocalRepository @Inject constructor(val context: Context) {
    fun getSearchHistories(): MutableList<SearchCoinHistory> {
        return SharePrefUtils.searchCoinHistories(context)
    }

    fun setSearchHistory(histories: MutableList<SearchCoinHistory>) {
        return SharePrefUtils.setSearchCoinHistory(context, histories)
    }

    fun removeCoinSearchHistoryById(id: String) {
        val coins = getSearchHistories()
        coins.removeAll {
            it.id == id
        }
        setSearchHistory(coins)
    }

    fun addCoinSearchHistoryList(coin: SearchCoinHistory) {
        val coins = getSearchHistories()
        coins.add(0, coin)
        setSearchHistory(coins)
    }

    fun getCoinWatchList(): MutableList<CoinWatchList> {
        return SharePrefUtils.coinWatchList(context)
    }

    fun getCoinWatchListById(id: String): CoinWatchList? {
        val coins = getCoinWatchList()
        return coins.find {
            it.id == id
        }
    }

    fun setCoinWatchList(coins: MutableList<CoinWatchList>) {
        return SharePrefUtils.setCoinWatchList(context, coins)
    }

    fun removeCoinWatchListById(id: String) {
        val coins = getCoinWatchList()
        coins.removeAll {
            it.id == id
        }
        setCoinWatchList(coins)
    }

    fun addCoinWatchList(coin: CoinWatchList) {
        val coins = getCoinWatchList()
        coins.add(0, coin)
        setCoinWatchList(coins)
    }
}