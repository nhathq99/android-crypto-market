package com.example.cryptomarket.data.models.local

sealed class CoinInfoItem(open val key: String) {

    companion object {
        const val TYPE_NORMAL = 0
        const val TYPE_LINKS = 1
    }

    data class NormalValue(override val key: String,
                          val value: String,): CoinInfoItem(key)
    data class LinkValue(override val key: String,
                         val links: MutableList<String>): CoinInfoItem(key)
}