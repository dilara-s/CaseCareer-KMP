package ru.kpfu.itis.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.koinInject
import ru.kpfu.itis.core.network.TokenStorage
import ru.kpfu.itis.feature.auth.AuthRoute

@Composable
fun AppNavGraph(
    tokenStorage: TokenStorage = koinInject()
) {
    val navController = rememberNavController()
    val startDestination = if (tokenStorage.isLoggedIn()) "main" else "auth"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("auth") {
            AuthRoute(navController = navController)
        }
        composable("main") {
            Text("Main screen — в разработке")
        }
    }
}