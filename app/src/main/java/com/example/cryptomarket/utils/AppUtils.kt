package com.example.cryptomarket.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.cryptomarket.bases.BaseApplication
import java.text.NumberFormat
import kotlin.math.log10
import kotlin.math.pow

class AppUtils {
    companion object {

        fun percentFormat(value: Number, maxDigits: Int = 1, minDigits: Int = 0) : String {
            val format = NumberFormat.getInstance()
            format.maximumFractionDigits = maxDigits
            format.minimumFractionDigits = minDigits
            return "${format.format(value)}%"

        }
        fun humanReadable(value: Long): String {
            log10(value.coerceAtLeast(1).toDouble()).toInt().div(3).let {
                val precision = when (it) {
                    0 -> 0; else -> 1
                }
                val suffix = arrayOf("", "K", "M", "B", "T", "Q")
                return String.format(
                    "%.${precision}f ${suffix[it]}",
                    value.toDouble() / 10.0.pow(it * 3)
                )
            }
        }
        fun hasInternetConnection(application: BaseApplication): Boolean {
            val connectivityManager = application.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val activeNetwork = connectivityManager.activeNetwork ?: return false
                val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
                return when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } else {
                connectivityManager.activeNetworkInfo?.run {
                    return when(type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
            return false
        }
    }
}