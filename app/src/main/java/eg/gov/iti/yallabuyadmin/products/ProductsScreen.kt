package eg.gov.iti.yallabuyadmin.products


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import eg.gov.iti.yallabuyadmin.model.Product


@Composable
fun ProductsScreen(navController: NavController, viewModel: ProductsViewModel) {

    val products = viewModel.products.value

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF2CABAB),
            Color(0xFFE1F5FE)
        )
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(remember { SnackbarHostState() }) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = backgroundBrush)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ProductsScreenUI(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                products
            )
        }
    }
}


@Composable
fun ProductsScreenUI(modifier: Modifier = Modifier, products: List<Product>) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LazyColumn {
            items(products) { product ->
                Text(text = product.title)
            }
        }

    }
}









