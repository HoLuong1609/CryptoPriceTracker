package com.crypto.pricetracker.presentation.navitgation

object NavRoutes {

    const val MARKET = "market"
    const val CHART = "chart/{${RouteArgs.SYMBOL}}"
    const val ORDERBOOK = "orderbook/{${RouteArgs.SYMBOL}}"
    const val TRADES = "trades/{${RouteArgs.SYMBOL}}"
}