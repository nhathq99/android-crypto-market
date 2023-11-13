package com.example.cryptomarket.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptomarket.data.models.MarketChart
import com.example.cryptomarket.data.models.Resource
import com.example.cryptomarket.data.models.coin_detail.CoinDetail
import com.example.cryptomarket.data.models.local.CoinWatchList
import com.example.cryptomarket.data.models.requests.CoinChartRangeRequest
import com.example.cryptomarket.data.models.requests.CoinDetailRequest
import com.example.cryptomarket.data.models.responses.CoinDetailResponse
import com.example.cryptomarket.repositories.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CoinDetailViewModel @Inject constructor(private val appRepository: AppRepository): ViewModel() {

    val coinDetailData = MutableLiveData<Resource<CoinDetailResponse>>()
    val watchListData = MutableLiveData<CoinWatchList?>()

    fun fetchCoinDetail(id: String, isLoading: Boolean = false) {
        viewModelScope.launch {
            if (isLoading) {
                coinDetailData.postValue(Resource.Loading())
            }
            try {
                val detailReq = async { appRepository.coins.getCoinDetail(id, CoinDetailRequest()) }
                val chartReq = async { appRepository.coins.getCoinChartRange(id, CoinChartRangeRequest.init()) }
                val result = awaitAll(detailReq, chartReq)

                val detailRes = result[0] as Response<CoinDetail>
                val charRes = result[1] as Response<MarketChart>

                if (!detailRes.isSuccessful || detailRes.body() == null) {
                    coinDetailData.postValue(Resource.Error(detailRes.message()))
                    return@launch
                }
                if (!charRes.isSuccessful || charRes.body() == null) {
                    coinDetailData.postValue(Resource.Error(charRes.message()))
                    return@launch
                }

                val data = CoinDetailResponse(
                    detailRes.body()!!,
                    charRes.body()!!,
                )
                coinDetailData.postValue(Resource.Success(data))
            } catch (t: Throwable) {
                Log.e("Throwable", "fetchCoinDetail: $t")
                coinDetailData.postValue(Resource.Error(t.message ?: ""))
            }
        }
    }

    fun checkWatchList(id: String) {
        viewModelScope.launch {
            val coin = appRepository.local.getCoinWatchListById(id)
            watchListData.postValue(coin)
        }
    }

    fun onWatchListChange() {
        var coin = watchListData.value

        if ( coin == null) {
            val detail = coinDetailData.value?.data?.coinDetail ?: return
            coin = CoinWatchList(detail.id, detail.name,detail.symbol, detail.image.thumb)
            appRepository.local.addCoinWatchList(coin)
            watchListData.postValue(coin)
            return
        }

        appRepository.local.removeCoinWatchListById(coin.id)
        watchListData.postValue(null)
    }
}