package at.str.lottery.barcode.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.*
import at.str.lottery.barcode.R
import at.str.lottery.barcode.model.ScanTrackerViewModel
import at.str.lottery.barcode.ui.scan.ScanScreen
import at.str.lottery.barcode.ui.settings.SettingsScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector, val imgContentDescriptor: String) {
    object Scan : Screen("scan", R.string.scan, Icons.Filled.Search, "Search")
    object Settings : Screen("settings", R.string.settings, Icons.Filled.Settings, "Settings")
}

@ExperimentalCoroutinesApi
@Composable
fun LotteryApp(viewModel: ScanTrackerViewModel) {
    LotteryBarcodeScannerTheme {
        // A surface container using the 'background' color from the theme
        val navController = rememberNavController()
        Surface(color = MaterialTheme.colors.background) {
            Scaffold(bottomBar = {
                    BottomNavigation {
                        val items = listOf(
                            Screen.Scan,
                            Screen.Settings
                        )

                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)
                        items.forEach { screen ->
                            BottomNavigationItem(
                                icon = { Icon(screen.icon, screen.imgContentDescriptor) },
                                label = { Text(stringResource(screen.resourceId)) },
                                selected = currentRoute == screen.route,
                                onClick = {
                                    // This is the equivalent to popUpTo the start destination
                                    navController.popBackStack(
                                        navController.graph.startDestination,
                                        false
                                    )
                                    // This if check gives us a "singleTop" behavior where we do not create a
                                    // second instance of the composable if we are already on that destination
                                    if (currentRoute != screen.route) {
                                        navController.navigate(screen.route)
                                    }
                                }
                            )
                        }
                    }
                }
            ) {
                // This is a simple app right now so the one global ViewModel is all we need.
                // However, in the future using Jetpack Navigation's support for scoped state
                // would be more appropriate.
                // See https://stackoverflow.com/q/64955859 and
                // https://developer.android.com/guide/navigation/navigation-programmatic#share_ui-related_data_between_destinations_with_viewmodel

                NavHost(navController, startDestination = Screen.Scan.route) {
                    composable(Screen.Scan.route) {
                        ScanScreen(navController, viewModel)
                    }
                    composable(Screen.Settings.route) {
                        SettingsScreen(navController, viewModel)
                    }
                }
            }
        }
    }
}