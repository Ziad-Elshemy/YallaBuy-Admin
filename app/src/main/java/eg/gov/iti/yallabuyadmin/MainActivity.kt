package eg.gov.iti.yallabuyadmin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import eg.gov.iti.yallabuyadmin.coupons.CouponsFactory
import eg.gov.iti.yallabuyadmin.coupons.CouponsScreen
import eg.gov.iti.yallabuyadmin.coupons.CouponsViewModel
import eg.gov.iti.yallabuyadmin.dashboard.DashboardFactory
import eg.gov.iti.yallabuyadmin.dashboard.DashboardScreen
import eg.gov.iti.yallabuyadmin.dashboard.DashboardViewModel
import eg.gov.iti.yallabuyadmin.database.LocalDataSourceImpl
import eg.gov.iti.yallabuyadmin.inventory.InventoryFactory
import eg.gov.iti.yallabuyadmin.inventory.InventoryScreen
import eg.gov.iti.yallabuyadmin.inventory.InventoryViewModel
import eg.gov.iti.yallabuyadmin.navigation.BottomNavigationBar
import eg.gov.iti.yallabuyadmin.navigation.NavigationRoute
import eg.gov.iti.yallabuyadmin.network.RemoteDataSourceImpl
import eg.gov.iti.yallabuyadmin.network.api.RetrofitInstance
import eg.gov.iti.yallabuyadmin.products.ProductsFactory
import eg.gov.iti.yallabuyadmin.products.ProductsScreen
import eg.gov.iti.yallabuyadmin.products.ProductsViewModel
import eg.gov.iti.yallabuyadmin.profile.ProfileFactory
import eg.gov.iti.yallabuyadmin.profile.ProfileScreen
import eg.gov.iti.yallabuyadmin.profile.ProfileViewModel
import eg.gov.iti.yallabuyadmin.ui.theme.YallaBuyAdminTheme
import eg.iti.mad.climaguard.repo.Repository
import eg.iti.mad.climaguard.repo.RepositoryImpl

class MainActivity : ComponentActivity() {

    private val TAG = "MainActivity"

    lateinit var repo : Repository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        repo = RepositoryImpl.getInstance(LocalDataSourceImpl(),RemoteDataSourceImpl(RetrofitInstance.api))

        val productsViewModel = ViewModelProvider(
            this@MainActivity,
            factory =ProductsFactory(repo)
        ).get(ProductsViewModel::class.java)

        val inventoryViewModel = ViewModelProvider(
            this@MainActivity,
            factory =InventoryFactory(repo)
        ).get(InventoryViewModel::class.java)

        val dashboardViewModel = ViewModelProvider(
            this@MainActivity,
            factory =DashboardFactory(repo)
        ).get(DashboardViewModel::class.java)

        val couponsViewModel = ViewModelProvider(
            this@MainActivity,
            factory =CouponsFactory(repo)
        ).get(CouponsViewModel::class.java)

        val profileViewModel = ViewModelProvider(
            this@MainActivity,
            factory =ProfileFactory(repo)
        ).get(ProfileViewModel::class.java)





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
                                ProductsScreen(navController, productsViewModel)
                            }
                            composable(route = NavigationRoute.Inventory.route) {
                                InventoryScreen(navController, inventoryViewModel)
                            }
                            composable(route = NavigationRoute.Dashboard.route) {
                                DashboardScreen(navController, dashboardViewModel)
                            }
                            composable(route = NavigationRoute.Coupons.route) {
                                CouponsScreen(navController, couponsViewModel)
                            }
                            composable(route = NavigationRoute.Profile.route) {
                                ProfileScreen(navController, profileViewModel)
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
