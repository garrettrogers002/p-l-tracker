package com.garrettrogers.pltracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.garrettrogers.pltracker.ui.screens.analysis.AnalysisScreen
import com.garrettrogers.pltracker.ui.screens.dashboard.DashboardScreen
import com.garrettrogers.pltracker.ui.screens.history.HistoryScreen
import com.garrettrogers.pltracker.ui.screens.trade.AddTradeScreen
import com.garrettrogers.pltracker.ui.screens.trade.CloseTradeScreen
import com.garrettrogers.pltracker.ui.screens.trade.TradeDetailsScreen

@Composable
fun PLTrackerNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Dashboard.route
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToAddTrade = { navController.navigate(Screen.AddTrade.route) },

                onNavigateToAnalysis = { navController.navigate(Screen.Analysis.route) },
                onNavigateToHistory = { navController.navigate(Screen.History.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onNavigateToCloseTrade = { tradeId ->
                    navController.navigate(Screen.CloseTrade.createRoute(tradeId))
                }
            )
        }
        composable(Screen.AddTrade.route) {
            AddTradeScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }


        composable(
            route = Screen.CloseTrade.route,
            arguments = listOf(navArgument("tradeId") { type = NavType.LongType })
        ) { backStackEntry ->
            val tradeId = backStackEntry.arguments?.getLong("tradeId")
            if (tradeId != null) {
                CloseTradeScreen(
                    tradeId = tradeId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
        composable(Screen.History.route) {
            HistoryScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetails = { tradeId ->
                    navController.navigate(Screen.TradeDetails.createRoute(tradeId))
                }
            )
        }
        composable(Screen.Analysis.route) {
            AnalysisScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(
            route = Screen.TradeDetails.route,
            arguments = listOf(navArgument("tradeId") { type = NavType.LongType })
        ) { backStackEntry ->
            val tradeId = backStackEntry.arguments?.getLong("tradeId")
            if (tradeId != null) {
                TradeDetailsScreen(
                    tradeId = tradeId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
        
        composable(Screen.Settings.route) {
            com.garrettrogers.pltracker.ui.screens.settings.SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
