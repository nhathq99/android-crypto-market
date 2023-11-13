package com.example.cryptomarket.data.models.requests

data class CoinMarketRequest(
    val vsCurrency: String = "usd",
    val order: String = "market_cap_desc",
    val perPage: Int = 10,
    val page: Int = 1,
    val locale: String = "en"
) {
    val params
        get(): HashMap<String, Any> {
            return hashMapOf(
                "vs_currency" to vsCurrency,
                "order" to order,
                "per_page" to perPage,
                "page" to page,
                "locale" to locale
            )
        }
}