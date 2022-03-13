package com.ares.aspiration.data.entity

import com.google.gson.JsonObject

sealed class LatestResponse

data class LatestSuccess(
    val list: List<RatesAttributes>
) : LatestResponse()

data class Latest(
    val success: Boolean,
    val timeStamp: Long,
    val base: String,
    val date: String,
    val rates: JsonObject,
    val error: JsonObject
)

data class RatesAttributes(
    val countryCode: String,
    val quantity: Double
)