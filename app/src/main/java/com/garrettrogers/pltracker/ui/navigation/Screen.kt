package com.garrettrogers.pltracker.ui.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object AddTrade : Screen("add_trade")
    object CloseTrade : Screen("close_trade/{tradeId}") {
        fun createRoute(tradeId: Long) = "close_trade/$tradeId"
    }
    object History : Screen("history")
    object Analysis : Screen("analysis")
}
