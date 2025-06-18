package eg.gov.iti.yallabuyadmin.dashboard


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import eg.gov.iti.yallabuyadmin.model.DashboardData
import eg.gov.iti.yallabuyadmin.model.Response
import eg.gov.iti.yallabuyadmin.products.LoadingIndicator
import eg.gov.iti.yallabuyadmin.R


@Composable
fun DashboardScreen(navController: NavController,viewModel: DashboardViewModel) {
    val state by viewModel.dashboardData.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchDashboardData()
    }

    when (state) {
        is Response.Loading -> LoadingIndicator()
        is Response.Failure -> Text("Failed to load dashboard", color = Color.Red)
        is Response.Success -> {
            val data = (state as Response.Success<DashboardData>).data
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                item { DashboardCard("Products", data.productCount, R.drawable.ic_products_dashboard) }
                item { DashboardCard("Total Product Items", data.inventoryItemsCount, R.drawable.ic_product_items_dashboard) }
                item { DashboardCard("Vendors", data.vendorsCount, R.drawable.ic_brand_dashboard) }
                item { DashboardCard("Price Rules", data.priceRuleCount, R.drawable.ic_price_rules_dashboard) }
                item { DashboardCard("Discounts", data.discountCount, R.drawable.ic_discounts_dashboard) }
            }
        }
    }
}

@Composable
fun DashboardCard(title: String, count: Int, iconRes: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(72.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Bold)
                Text(text = "Number of Items: $count")
            }
        }
    }
}











