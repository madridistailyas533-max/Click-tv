package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.theme.RedAccent
import com.example.viewmodel.AppViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "الرئيسية", Icons.Default.Home)
    object Live : Screen("live", "مباشر", Icons.Default.LiveTv)
    object Movies : Screen("movies", "أفلام", Icons.Default.Movie)
    object Series : Screen("series", "مسلسلات", Icons.Default.Tv)
    object Favorites : Screen("favorites", "مكتبتي", Icons.Default.Favorite)
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: AppViewModel = viewModel()
    
    val screens = listOf(
        Screen.Home,
        Screen.Live,
        Screen.Movies,
        Screen.Series,
        Screen.Favorites
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            
            // Only show bottom bar on main screens
            if (screens.any { it.route == currentRoute } || currentRoute == null) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                    windowInsets = WindowInsets.navigationBars
                ) {
                    screens.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.route || (currentRoute == null && screen == Screen.Home),
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = RedAccent,
                                selectedTextColor = MaterialTheme.colorScheme.onBackground,
                                indicatorColor = MaterialTheme.colorScheme.surfaceVariant,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            NavHost(navController = navController, startDestination = Screen.Home.route) {
                composable(Screen.Home.route) { HomeScreen(navController, viewModel) }
                composable(Screen.Live.route) { LiveScreen(navController, viewModel) }
                composable(Screen.Movies.route) { FilteredScreen(navController, viewModel, com.example.models.ItemType.MOVIE) }
                composable(Screen.Series.route) { FilteredScreen(navController, viewModel, com.example.models.ItemType.SERIES) }
                composable(Screen.Favorites.route) { FavoritesScreen(navController, viewModel) }
                
                composable("details/{id}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")
                    val item = com.example.data.MockData.vodContent.find { it.id == id }
                    if (item != null) {
                        VodDetailsScreen(item, navController, viewModel)
                    }
                }
                
                composable("live_category/{id}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")
                    val category = com.example.data.MockData.liveCategories.find { it.id == id }
                    if (category != null) {
                        LiveCategoryScreen(category, navController)
                    }
                }
                
                composable("player/{url}/{title}") { backStackEntry ->
                    val url = java.net.URLDecoder.decode(backStackEntry.arguments?.getString("url") ?: "", "UTF-8")
                    val title = backStackEntry.arguments?.getString("title") ?: ""
                    PlayerScreen(url, title, navController)
                }
            }
        }
    }
}
