package com.ares.aspiration.abstraction.util

object Constants {

    const val BASE = "base"
    const val SYMBOLS_END_POINT = "symbols"
    const val LATEST_END_POINT = "latest"
    const val INFO = "info"
    const val CODE = "code"
    const val CURRENCY_RESTRICTED_ERROR = 105
}

enum class SOURCE {
    SYMBOLS,
    LATEST
}