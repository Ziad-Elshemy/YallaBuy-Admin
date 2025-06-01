package eg.gov.iti.yallabuyadmin.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource

import eg.gov.iti.yallabuyadmin.R

sealed class NavigationRoute(val route: String) {
    object Products : NavigationRoute("products_screen")
    object Inventory : NavigationRoute("Inventory_screen")
    object Dashboard : NavigationRoute("dashboard_screen")
    object Coupons : NavigationRoute("about_screen")
    object Profile : NavigationRoute("profile_screen")

}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navigationItems = listOf(
        NavigationItem("Products", painterResource(id = R.drawable.ic_products), NavigationRoute.Products.route),
        NavigationItem("Inventory", painterResource(id = R.drawable.ic_inventory), NavigationRoute.Inventory.route),
        NavigationItem("Dashboard", painterResource(id = R.drawable.ic_dashboard), NavigationRoute.Dashboard.route),
        NavigationItem("Coupons", painterResource(id = R.drawable.ic_coupons), NavigationRoute.Coupons.route),
        NavigationItem("Profile", painterResource(id = R.drawable.ic_profile), NavigationRoute.Profile.route)
    )

    val currentDestination = navController.currentBackStackEntryAsState().value
    val selectedNavigationIndex = navigationItems.indexOfFirst {
        it.route == currentDestination?.destination?.route
    }.coerceAtLeast(0)

    val middleIndex = 2

    Box {
        NavigationBar(containerColor = Color(0xFFB0BEC5)) {
            navigationItems.forEachIndexed { index, item ->
                if (index == middleIndex) {
                    Spacer(Modifier.weight(1f)) // horizontal space for the floating btn
                } else {
                    NavigationBarItem(
                        selected = selectedNavigationIndex == index,
                        onClick = {
                            if (currentDestination?.destination?.route != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                painter = item.icon,
                                contentDescription = item.title,
                                tint = if (index == selectedNavigationIndex)
                                    Color(0xFF2CABAB)
                                else
                                    Color(0xFF4F585D)
                            )
                        },
                        label = {
                            Text(
                                item.title,
                                color = if (index == selectedNavigationIndex)
                                    Color(0xFF2CABAB)
                                else
                                    Color(0xFF4F585D)
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color(0xFFB0BEC5)
                        )
                    )
                }
            }
        }

        //centered dashboard btn
        val isSelected = currentDestination?.destination?.route == NavigationRoute.Dashboard.route

        FloatingActionButton(
            onClick = {
                navController.navigate(NavigationRoute.Dashboard.route) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            containerColor = Color.White,
            elevation = FloatingActionButtonDefaults.elevation(6.dp),
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-18).dp)
        ) {
            Icon(
                painter = navigationItems[middleIndex].icon,
                contentDescription = "Dashboard",
                tint = if (isSelected) Color(0xFF2CABAB) else Color(0xFF4F585D)
            )
        }

    }
}

