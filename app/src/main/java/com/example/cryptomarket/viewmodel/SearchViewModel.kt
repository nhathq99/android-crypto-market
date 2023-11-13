package com.example.cryptomarket.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptomarket.data.models.Resource
import com.example.cryptomarket.data.models.local.SearchCoinHistory
import com.example.cryptomarket.data.models.responses.SearchResponse
import com.example.cryptomarket.data.models.responses.SearchTrendingResponse
import com.example.cryptomarket.repositories.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val appRepository: AppRepository) : ViewModel() {
    val searchHistoryAndTrendingData = MutableLiveData<Resource<Pair<MutableList<SearchCoinHistory>, SearchTrendingResponse>>>()
    val searchData = MutableLiveData<Resource<SearchResponse>>()

    var searchHistoryAndTrending: Pair<MutableList<SearchCoinHistory>, SearchTrendingResponse>? = null

    fun fetchSearchHistoryAndTrending() {
        viewModelScope.launch {
            searchHistoryAndTrendingData.postValue(Resource.Loading())
            try {
                val res = appRepository.coins.getSearchTrending()
                if (res.isSuccessful && res.body() != null) {
                    val trending = res.body()!!
                    val histories = appRepository.local.getSearchHistories()
                     val data = Pair(histories, trending)
                    searchHistoryAndTrending = data
                    searchHistoryAndTrendingData.postValue(Resource.Success(data))
                } else {
                    searchHistoryAndTrendingData.postValue(Resource.Error(res.message()))
                }
            } catch (t: Throwable) {
                searchHistoryAndTrendingData.postValue(Resource.Error(t.message ?: ""))
            }
        }
    }

    fun fetchSearch(query: String) {
        viewModelScope.launch {
            searchData.postValue(Resource.Loading())
            try {
                val res = appRepository.coins.getSearch(query)
                if (res.isSuccessful && res.body() != null) {

                    searchData.postValue(Resource.Success(res.body()!!))
                } else {
                    searchData.postValue(Resource.Error(res.message()))
                }
            } catch (t: Throwable) {
                searchData.postValue(Resource.Error(t.message ?: ""))
            }
        }
    }

    fun addHistorySearch(coin: SearchCoinHistory) {
        appRepository.local.addCoinSearchHistoryList(coin)
        val coins = searchHistoryAndTrending?.first ?: return
        coins.add(coin)
        val trending = searchHistoryAndTrending!!.second
        val data = Pair(coins, trending)
        searchHistoryAndTrending = data
    }

    fun removeHistoryByPosition(pos: Int) {
        val coins = searchHistoryAndTrending?.first
        val coin = coins?.get(pos) ?: return
        appRepository.local.removeCoinSearchHistoryById(coin.id)
        coins.removeAt(pos)
        val trending = searchHistoryAndTrending!!.second
        val data = Pair(coins, trending)
        searchHistoryAndTrending = data
        searchHistoryAndTrendingData.postValue(Resource.Success(data))
    }

    fun clearAllSearchHistory() {
        val coins = mutableListOf<SearchCoinHistory>()
        appRepository.local.setSearchHistory(coins)
        val trending = searchHistoryAndTrending!!.second
        val data = Pair(coins, trending)
        searchHistoryAndTrending = data
        searchHistoryAndTrendingData.postValue(Resource.Success(data))
    }
}