package eg.gov.iti.yallabuyadmin.inventory


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import eg.gov.iti.yallabuyadmin.model.InventoryItemUiModel
import eg.gov.iti.yallabuyadmin.model.Response
import eg.gov.iti.yallabuyadmin.products.LoadingIndicator


@Composable
fun InventoryScreen(navController: NavController,
                    viewModel: InventoryViewModel) {
    val uiState by viewModel.inventoryItems.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchInventoryItems()
    }

    when (uiState) {
        is Response.Loading -> LoadingIndicator()
        is Response.Failure -> Text("Failed to load inventory", color = Color.Red)
        is Response.Success -> {
            val items = (uiState as Response.Success<List<InventoryItemUiModel>>).data
            LazyColumn {
                items(items) { item ->
                    InventoryItemCard(item)
                }
            }
        }
    }
}

@Composable
fun InventoryItemCard(item: InventoryItemUiModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(12.dp))

            Column {
                Text(item.title, fontWeight = FontWeight.Bold)
                Text(item.variantTitle)
                Text("Item Id: ${item.inventoryItemId}")
                Text("Price: ${item.price}")
                Text("Quantity: ${item.quantity}")
            }
        }
    }
}










