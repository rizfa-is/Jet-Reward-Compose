package com.issog.jetrewardcompose.ui.navigation

sealed class Screen(val route: String) {
    data object Home: Screen("Home")
    data object Cart: Screen("Cart")
    data object Profile: Screen("Profile")
    data object DetailReward: Screen("home/{rewardId}") {
        fun createRoute(rewardId: Long) = "home/$rewardId"
    }
}