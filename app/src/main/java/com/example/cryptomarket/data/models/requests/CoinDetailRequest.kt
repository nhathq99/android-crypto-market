package com.example.cryptomarket.data.models.requests

data class CoinDetailRequest(
    val localization: String = "false",
) {
    val params
        get(): HashMap<String, Any> {
            return hashMapOf(
                "localization" to localization,
            )
        }
}