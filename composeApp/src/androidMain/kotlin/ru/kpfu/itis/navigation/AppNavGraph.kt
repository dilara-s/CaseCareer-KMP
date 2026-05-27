package ru.kpfu.itis.navigation

import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.koin.compose.koinInject
import ru.kpfu.itis.R
import ru.kpfu.itis.core.network.TokenStorage
import ru.kpfu.itis.designSystem.Primary
import ru.kpfu.itis.feature.auth.AuthRoute
import ru.kpfu.itis.feature.feed.CaseDetailRoute
import ru.kpfu.itis.feature.feed.FeedRoute
import ru.kpfu.itis.feature.mycases.MyCasesRoute
import ru.kpfu.itis.feature.profile.ProfileRoute
import ru.kpfu.itis.feature.response.ResponseRoute

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val iconRes: Int,
    val iconActiveRes: Int
) {
    object Feed : BottomNavItem("feed", "Главная", R.drawable.ic_home, R.drawable.ic_home)
    object MyCases : BottomNavItem("my_cases", "Мои кейсы", R.drawable.ic_bookmark, R.drawable.ic_bookmark)
    object Account : BottomNavItem("account", "Аккаунт", R.drawable.ic_person, R.drawable.ic_person)
}

private val bottomNavItems = listOf(
    BottomNavItem.Feed,
    BottomNavItem.MyCases,
    BottomNavItem.Account
)

private val routesWithoutBottomNav = setOf("auth", "response")

@Composable
fun AppNavGraph(
    tokenStorage: TokenStorage = koinInject()
) {
    val navController = rememberNavController()
    val startDestination = if (tokenStorage.isLoggedIn()) "feed" else "auth"
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = routesWithoutBottomNav.none { currentRoute?.startsWith(it) == true }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                CaseCareerBottomBar(
                    currentRoute = currentRoute,
                    onItemClick = { item ->
                        navController.navigate(item.route) {
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
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("auth") {
                AuthRoute(navController = navController)
            }
            composable("feed") {
                FeedRoute(onCaseClick = { caseId ->
                    navController.navigate("case_detail/$caseId")
                })
            }
            composable("case_detail/{caseId}") { backStackEntry ->
                val caseId = backStackEntry.arguments?.getString("caseId")?.toLongOrNull() ?: return@composable
                CaseDetailRoute(
                    caseId = caseId,
                    onBack = { navController.popBackStack() },
                    onApplyNda = { id, title, company ->
                        navController.navigate(
                            "response/$id?ndaRequired=true&caseTitle=${Uri.encode(title)}&companyName=${Uri.encode(company)}"
                        )
                    },
                    onApplyForm = { id, title, company ->
                        navController.navigate(
                            "response/$id?ndaRequired=false&caseTitle=${Uri.encode(title)}&companyName=${Uri.encode(company)}"
                        )
                    }
                )
            }
            composable(
                route = "response/{caseId}?ndaRequired={ndaRequired}&caseTitle={caseTitle}&companyName={companyName}",
                arguments = listOf(
                    navArgument("caseId") { type = NavType.IntType },
                    navArgument("ndaRequired") { type = NavType.BoolType; defaultValue = false },
                    navArgument("caseTitle") { type = NavType.StringType; defaultValue = "" },
                    navArgument("companyName") { type = NavType.StringType; defaultValue = "" }
                )
            ) { backStackEntry ->
                val caseId = backStackEntry.arguments?.getInt("caseId") ?: return@composable
                val ndaRequired = backStackEntry.arguments?.getBoolean("ndaRequired") ?: false
                val caseTitle = backStackEntry.arguments?.getString("caseTitle") ?: ""
                val companyName = backStackEntry.arguments?.getString("companyName") ?: ""
                ResponseRoute(
                    caseId = caseId,
                    caseTitle = caseTitle,
                    companyName = companyName,
                    ndaRequired = ndaRequired,
                    navController = navController
                )
            }
            composable("my_cases") {
                MyCasesRoute()
            }
            composable("account") {
                ProfileRoute(navController = navController)
            }
        }
    }
}

@Composable
private fun CaseCareerBottomBar(
    currentRoute: String?,
    onItemClick: (BottomNavItem) -> Unit
) {
    NavigationBar(
        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
        tonalElevation = androidx.compose.ui.unit.Dp(0f)
    ) {
        bottomNavItems.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = { onItemClick(item) },
                icon = {
                    Icon(
                        painter = painterResource(item.iconRes),
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Primary,
                    selectedTextColor = Primary,
                    unselectedIconColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
