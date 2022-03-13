package com.ares.aspiration.abstraction.util

object Constants {

    const val CURRENCY = "currency"
    const val PROCEED = "Proceed"
    const val CANCEL = "Cancel"
    const val ERROR_MESSAGE = "Sorry, there is an error..."
    const val SYMBOLS_END_POINT = "symbols"
    const val LATEST_END_POINT = "latest"
    const val INFO = "info"
    const val TYPE = "type"
}

enum class SOURCE {
    SYMBOLS,
    LATEST
}