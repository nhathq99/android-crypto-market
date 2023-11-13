package com.example.cryptomarket.data.models.local

sealed class PriceChartInfoItem(open val key: String) {

    companion object {
        const val TYPE_SINGLE_VALUE = 0
        const val TYPE_DATE_WITH_PRICE_VALUE = 1
    }

    data class TypeSingleValue(
        override val key: String,
        val value: String,
    ): PriceChartInfoItem(key)
    data class TypeDateWithPriceValue (
        override val key: String,
        val price: Double,
        val pricePercent: Double,
        val time: String,
    ): PriceChartInfoItem(key)
}