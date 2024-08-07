package com.issog.jetrewardcompose.ui.navigation

sealed class Screen(val route: String) {
    data object Home: Screen("Home")
    data object Cart: Screen("Cart")
    data object Profile: Screen("Profile")
}