package com.example.cryptomarket.data.models

data class Paging (
    var page: Int = 1,
    var isLoading: Boolean = false,
    var isRefreshing: Boolean = false,
    var hasReachedMax: Boolean = false,
    var totalPages: Int = 1,
    var perPage: Int = 10,
)
