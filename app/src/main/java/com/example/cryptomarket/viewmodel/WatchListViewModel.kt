package com.example.cryptomarket.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptomarket.data.models.Resource
import com.example.cryptomarket.data.models.local.CoinWatchList
import com.example.cryptomarket.repositories.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchListViewModel @Inject constructor(private val appRepository: AppRepository) : ViewModel() {
    var coinWatchListData = MutableLiveData<Resource<MutableList<CoinWatchList>>>()

    fun fetchCoinWatchList() {
        viewModelScope.launch {
            coinWatchListData.postValue(Resource.Loading())
            try {
                val watchList = appRepository.local.getCoinWatchList()
                coinWatchListData.postValue(Resource.Success(watchList))
            } catch (t: Throwable) {
                coinWatchListData.postValue(Resource.Error(t.message ?: ""))
            }
        }
    }

    fun removeWatchListItemByPosition(pos: Int) {
        val coins = coinWatchListData.value?.data ?: return
        val coin = coins[pos]
        appRepository.local.removeCoinWatchListById(coin.id)
        coins.removeAt(pos)
        coinWatchListData.postValue(Resource.Success(coins))
    }

}