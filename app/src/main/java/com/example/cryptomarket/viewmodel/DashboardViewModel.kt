package com.example.cryptomarket.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptomarket.data.models.CoinMarket
import com.example.cryptomarket.data.models.responses.CoinMarketResponse
import com.example.cryptomarket.data.models.Resource
import com.example.cryptomarket.data.models.requests.CoinMarketRequest
import com.example.cryptomarket.repositories.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val appRepository: AppRepository) : ViewModel() {

    val coinMarketData = MutableLiveData<Resource<CoinMarketResponse>>()
    private var coinMarkets = mutableListOf<CoinMarket>()

    fun fetchCoinMarkets(request: CoinMarketRequest, isLoading: Boolean = false) {
        viewModelScope.launch {
            if (isLoading) {
                coinMarketData.postValue(Resource.Loading())
            }
            try {
                val res = appRepository.coins.getCoinMarketList(request)
                if (res.isSuccessful) {
                    if (request.page == 1) {
                        coinMarkets.clear()
                    }
                    val list = res.body() ?: listOf()
                    coinMarkets.addAll(list)
                    val data = CoinMarketResponse(
                        coinMarkets = coinMarkets,
                        currentPage = request.page,
                        hasReachedMax = list.size != request.perPage,
                    )
                    coinMarketData.postValue(Resource.Success(data))
                } else {
                    coinMarketData.postValue(Resource.Error(res.message()))
                    Log.e("Error", "fetchCoinMarkets: ${res.message()}")
                    Log.e("errorBody", "fetchCoinMarkets: ${res.errorBody()}")
                }
            } catch (t: Throwable) {
                Log.e("Throwable", "fetchCoinMarkets: $t")
                coinMarketData.postValue(Resource.Error(t.message ?: ""))
            }
        }
    }

}