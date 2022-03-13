package com.ares.aspiration.data.entity

import com.google.gson.JsonObject

sealed class SymbolsResponse

data class SymbolsSuccess(
    val list: List<SymbolsAttributes>
) : SymbolsResponse()

data class Symbol(
    val success: Boolean,
    val symbols: JsonObject,
    val error: JsonObject
)

data class SymbolsAttributes(
    val currencyCode: String,
    val countryName: String
)