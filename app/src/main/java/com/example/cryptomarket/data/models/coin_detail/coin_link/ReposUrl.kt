package com.example.cryptomarket.data.models.coin_detail.coin_link

import com.google.gson.annotations.SerializedName

data class ReposUrl(
    @SerializedName("bitbucket")
    val bitbucket: List<Any>,
    @SerializedName("github")
    val github: List<String>
)