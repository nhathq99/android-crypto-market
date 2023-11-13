package com.example.cryptomarket.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.cryptomarket.constants.SharePrefConstants
import com.example.cryptomarket.data.models.local.CoinWatchList
import com.example.cryptomarket.data.models.local.SearchCoinHistory
import com.google.gson.Gson

class SharePrefUtils {
    companion object {
        fun setIgnoreOnBoarding(context: Context, ignore: Boolean) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putBoolean(SharePrefConstants.ignoreOnBoarding, ignore)
            editor.apply()
        }

        fun ignoreOnBoarding(context: Context): Boolean {
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            return pref.getBoolean(SharePrefConstants.ignoreOnBoarding, false)
        }

        fun setSearchCoinHistory(context: Context, histories: MutableList<SearchCoinHistory>) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val gson = Gson()
            val stringJson = gson.toJson(histories.map {
                gson.toJson(it)
            })
            editor.putString(SharePrefConstants.searchCoinHistory, stringJson)
            editor.apply()
        }

        fun searchCoinHistories(context: Context): MutableList<SearchCoinHistory> {
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            val stringJson = pref.getString(SharePrefConstants.searchCoinHistory, "")
            if (stringJson.isNullOrEmpty()) {
                return mutableListOf()
            }

            val gson = Gson()
            val json = gson.fromJson<MutableList<String>>(stringJson, MutableList::class.java)
            return json.map {
                gson.fromJson(it, SearchCoinHistory::class.java)
            }.toMutableList()
        }

        fun setCoinWatchList(context: Context, histories: MutableList<CoinWatchList>) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val gson = Gson()
            val stringJson = gson.toJson(histories.map {
                gson.toJson(it)
            })
            editor.putString(SharePrefConstants.coinWatchList, stringJson)
            editor.apply()
        }

        fun coinWatchList(context: Context): MutableList<CoinWatchList> {
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            val stringJson = pref.getString(SharePrefConstants.coinWatchList, "")
            if (stringJson.isNullOrEmpty()) {
                return mutableListOf()
            }

            val gson = Gson()
            val json = gson.fromJson<MutableList<String>>(stringJson, MutableList::class.java)
            return json.map {
                gson.fromJson(it, CoinWatchList::class.java)
            }.toMutableList()
        }
    }
}