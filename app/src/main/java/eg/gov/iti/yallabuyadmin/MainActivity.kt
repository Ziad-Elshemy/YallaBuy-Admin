package eg.gov.iti.yallabuyadmin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import eg.gov.iti.yallabuyadmin.coupons.CouponsScreen
import eg.gov.iti.yallabuyadmin.coupons.CouponsViewModel
import eg.gov.iti.yallabuyadmin.dashboard.DashboardScreen
import eg.gov.iti.yallabuyadmin.dashboard.DashboardViewModel
import eg.gov.iti.yallabuyadmin.inventory.InventoryScreen
import eg.gov.iti.yallabuyadmin.inventory.InventoryViewModel
import eg.gov.iti.yallabuyadmin.login.LoginScreen
import eg.gov.iti.yallabuyadmin.navigation.BottomNavigationBar
import eg.gov.iti.yallabuyadmin.navigation.NavigationRoute
import eg.gov.iti.yallabuyadmin.productdetails.ProductDetailsScreen
import eg.gov.iti.yallabuyadmin.productdetails.ProductDetailsViewModel
import eg.gov.iti.yallabuyadmin.products.ProductsScreen
import eg.gov.iti.yallabuyadmin.products.ProductsViewModel
import eg.gov.iti.yallabuyadmin.profile.ProfileScreen
import eg.gov.iti.yallabuyadmin.profile.ProfileViewModel
import eg.gov.iti.yallabuyadmin.ui.theme.YallaBuyAdminTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            val navController = rememberNavController()

            YallaBuyAdminTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    bottomBar = { BottomNavigationBar(navController) }
                ) { innerPadding ->

                    val graph =
                        navController.createGraph(startDestination = NavigationRoute.Dashboard.route) {

                            composable(route = NavigationRoute.Products.route) {
                                val productsViewModel : ProductsViewModel = koinViewModel()
                                ProductsScreen(navController, productsViewModel)
                            }
                            composable(route = NavigationRoute.Inventory.route) {
                                val inventoryViewModel : InventoryViewModel = koinViewModel()
                                InventoryScreen(navController, inventoryViewModel)
                            }
                            composable(route = NavigationRoute.Dashboard.route) {
                                val dashboardViewModel : DashboardViewModel = koinViewModel()
                                DashboardScreen(navController, dashboardViewModel)
                            }
                            composable(route = NavigationRoute.Coupons.route) {
                                val couponsViewModel : CouponsViewModel = koinViewModel()
                                CouponsScreen(navController, couponsViewModel)
                            }
                            composable(route = NavigationRoute.Profile.route) {
                                val profileViewModel : ProfileViewModel = koinViewModel()
                                ProfileScreen(navController, profileViewModel)
                            }

                            composable(route = NavigationRoute.Login.route) {
                                LoginScreen(navController)
                            }

                            composable(route = NavigationRoute.ProductDetails.route) { navBackStackEntry ->
                                val productId: Long = navBackStackEntry.arguments?.getString("id")
                                    ?.toLongOrNull() ?: 11916346917182
                                val productDetailsViewModel : ProductDetailsViewModel = koinViewModel()
                                ProductDetailsScreen(navController,productDetailsViewModel, productId)
                            }

                        }
                    NavHost(
                        navController = navController,
                        graph = graph,
                        modifier = Modifier.padding(innerPadding)
                    )

                }
            }
        }
    }
}
