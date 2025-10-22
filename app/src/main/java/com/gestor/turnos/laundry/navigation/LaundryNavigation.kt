package com.gestor.turnos.laundry.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gestor.turnos.laundry.R

sealed class Screen(val route: String, val icon: ImageVector, val labelRes: Int) {
    data object Home : Screen("home", Icons.Filled.Home, R.string.nav_home)
    data object Calendar : Screen("calendar", Icons.Filled.CalendarToday, R.string.nav_calendar)
    data object Bookings : Screen("bookings", Icons.Filled.List, R.string.nav_bookings)
    data object Admin : Screen("admin", Icons.Filled.Settings, R.string.nav_admin)
}

@Composable
fun LaundryNavigation() {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Home,
        Screen.Calendar,
        Screen.Bookings,
        Screen.Admin
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(stringResource(screen.labelRes)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
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
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(navController = navController)
            }
            composable(Screen.Calendar.route) {
                CalendarScreen()
            }
            composable(Screen.Bookings.route) {
                BookingsScreen()
            }
            composable(Screen.Admin.route) {
                AdminScreen()
            }
        }
    }
}

@Composable
fun HomeScreen(navController: androidx.navigation.NavController) {
    com.gestor.turnos.user.home.HomeScreen(
        onNavigateToCalendar = {
            navController.navigate(Screen.Calendar.route)
        }
    )
}

@Composable
fun CalendarScreen() {
    com.gestor.turnos.user.calendar.CalendarScreen()
}

@Composable
fun BookingsScreen() {
    com.gestor.turnos.user.bookings.BookingsScreen()
}

@Composable
fun AdminScreen() {
    Text("Admin Screen - Panel de administración")
}