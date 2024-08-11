package com.issog.jetrewardcompose

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.issog.jetrewardcompose.ui.navigation.NavigationItem
import com.issog.jetrewardcompose.ui.navigation.Screen
import com.issog.jetrewardcompose.ui.screen.cart.CartScreen
import com.issog.jetrewardcompose.ui.screen.detail.DetailRewardScreen
import com.issog.jetrewardcompose.ui.screen.home.HomeScreen
import com.issog.jetrewardcompose.ui.screen.profile.ProfileScreen
import com.issog.jetrewardcompose.ui.theme.JetRewardComposeTheme

@Composable
fun JetRewardApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute  = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.DetailReward.route) {
                BottomBar(navController, currentRoute)
            }
        },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    navigateToDetail = { rewardId ->
                        navController.navigate(Screen.DetailReward.createRoute(rewardId))
                    }
                )
            }
            composable(Screen.Cart.route) {
                val context = LocalContext.current
                CartScreen(
                    onOrderButtonClicked = { message ->
                        shareOrder(context, message)

                    }
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
            composable(
                route = Screen.DetailReward.route,
                arguments = listOf(navArgument("rewardId") { type = NavType.LongType })
            ) {
                val rewardId = it.arguments?.getLong("rewardId") ?: -1L
                DetailRewardScreen(
                    rewardId = rewardId,
                    navigateBack = { navController.navigateUp() },
                    navigateToCart = {
                        navController.popBackStack()
                        navController.navigate(Screen.Cart.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}

private fun shareOrder(context: Context, summary: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.dicoding_reward))
        putExtra(Intent.EXTRA_TEXT, summary)
    }
    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.dicoding_reward)
        )
    )
}

@Composable
fun BottomBar(
    navController: NavHostController,
    currentRoute: String?,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
    ) {

        val navigationItems = listOf(
            NavigationItem(
                title = stringResource(R.string.menu_home),
                icon = Icons.Default.Home,
                screen = Screen.Home
            ),
            NavigationItem(
                title = stringResource(R.string.menu_cart),
                icon = Icons.Default.ShoppingCart,
                screen = Screen.Cart
            ),
            NavigationItem(
                title = stringResource(R.string.menu_profile),
                icon = Icons.Default.AccountCircle,
                screen = Screen.Profile
            )
        )
        navigationItems.map { navigation ->
            NavigationBarItem(
                label = {
                    Text(text = navigation.title)
                },
                icon = {
                    Icon(
                        imageVector = navigation.icon,
                        contentDescription = navigation.title
                    )
                },
                selected = currentRoute == navigation.screen.route,
                onClick = {
                    navController.navigate(navigation.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JetRewardAppPreview() {
    JetRewardComposeTheme {
        JetRewardApp()
    }
}