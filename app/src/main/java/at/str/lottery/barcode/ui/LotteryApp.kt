package at.str.lottery.barcode.ui

import android.view.Gravity
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Absolute.Center
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import at.str.lottery.barcode.R
import at.str.lottery.barcode.model.ScanTrackerViewModel
import at.str.lottery.barcode.ui.scan.ScanScreen

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Scan : Screen("scan", R.string.scan, Icons.Filled.Search)
    object Settings : Screen("settings", R.string.settings, Icons.Filled.Settings)
}

@Composable
fun SettingsScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Lottery Barcode Scanner", style = MaterialTheme.typography.h5)
    }
}

@Composable
fun LotteryApp(scanTrackerViewModel: ScanTrackerViewModel) {
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
                                icon = { Icon(screen.icon) },
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
                NavHost(navController, startDestination = Screen.Scan.route) {
                    composable(Screen.Scan.route) { ScanScreen(navController, scanTrackerViewModel) }
                    composable(Screen.Settings.route) { SettingsScreen(navController) }
                }
            }
        }
    }
}