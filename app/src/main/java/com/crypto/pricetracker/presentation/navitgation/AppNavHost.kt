package com.crypto.pricetracker.presentation.navitgation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.crypto.pricetracker.presentation.chart.ChartRoute
import com.crypto.pricetracker.presentation.market.MarketListScreen
import com.crypto.pricetracker.presentation.orderbook.OrderBookScreen
import com.crypto.pricetracker.presentation.trades.TradesScreen

@Composable
fun AppNavHost() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.MARKET
    ) {

        composable(NavRoutes.MARKET) {

            MarketListScreen(
                onMarketClick = { symbol ->
                    navController.navigate("chart/$symbol")
                }
            )
        }

        composable(NavRoutes.CHART) { backStack ->

            val symbol = backStack.arguments?.getString(RouteArgs.SYMBOL) ?: ""

            ChartRoute(
                symbol = symbol,
                onBackClick = { navController.navigateUp() },
                modifier = Modifier.fillMaxSize()
            )
        }

        composable(NavRoutes.ORDERBOOK) { _ ->
            OrderBookScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(NavRoutes.TRADES) { _ ->
            TradesScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }
}