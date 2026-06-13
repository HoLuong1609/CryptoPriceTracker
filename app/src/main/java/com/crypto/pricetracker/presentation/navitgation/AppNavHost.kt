package com.crypto.pricetracker.presentation.navitgation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.crypto.pricetracker.presentation.detail.CoinDetailScreen
import com.crypto.pricetracker.presentation.market.MarketListScreen

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
                    navController.navigate("detail/$symbol")
                }
            )
        }

        composable(NavRoutes.DETAIL) { backStack ->
            val symbol = backStack.arguments?.getString(RouteArgs.SYMBOL) ?: ""

            CoinDetailScreen(
                symbol = symbol,
                onBackClick = { navController.navigateUp() },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}