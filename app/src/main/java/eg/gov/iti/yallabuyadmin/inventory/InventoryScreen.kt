package eg.gov.iti.yallabuyadmin.inventory


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import eg.gov.iti.yallabuyadmin.navigation.NavigationRoute
import eg.gov.iti.yallabuyadmin.products.LoadingIndicator


@Composable
fun InventoryScreen(
    navController: NavController,
    viewModel: InventoryViewModel
) {
    val uiState by viewModel.inventoryItems.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchInventoryItems()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Header Row with Add and Search Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Inventory Items",
                style = MaterialTheme.typography.titleLarge
            )
            Row {
//                IconButton(onClick = { /* Search Click */ }) {
//                    Icon(Icons.Default.Search, contentDescription = "Search")
//                }
            }

        }

        Spacer(modifier = Modifier.height(12.dp))

        when (uiState) {
            is Response.Loading -> LoadingIndicator()
            is Response.Failure -> Text("Failed to load inventory", color = Color.Red)
            is Response.Success -> {
                val items = (uiState as Response.Success<List<InventoryItemUiModel>>).data
                LazyColumn {
                    items(items) { item ->
                        InventoryItemCard(
                            item = item,
                            onUpdateQuantity = { newQuantity ->
                                viewModel.updateVariantQuantity(item.inventoryItemId, newQuantity)
                            })
                    }
                }
            }
        }
    }
}

@Composable
fun InventoryItemCard(
    item: InventoryItemUiModel,
    onUpdateQuantity: (Int) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { showDialog = true }, // open popup
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContentColor = Color.White,
            disabledContainerColor = Color.White
        ),
    ) {
        // Layout here (title, quantity, etc.)
        Row(modifier = Modifier.padding(16.dp)) {
            item.imageUrl?.let { image ->
                AsyncImage(
                    model = image,
                    contentDescription = null,
                    modifier = Modifier
                        .width(72.dp)
                        .height(120.dp)
                )
                Spacer(Modifier.width(8.dp))
            }

            Column {
                Text(item.title, fontWeight = FontWeight.Bold)
                Text(item.variantTitle)
                Text("Item ID: ${item.inventoryItemId}")
                Text("Quantity: ${item.quantity}")
            }
        }
    }

    if (showDialog) {
        QuantityUpdateDialog(
            productName = item.title,
            initialQuantity = item.quantity,
            onDismiss = { showDialog = false },
            onConfirm = { newQty ->
                showDialog = false
                onUpdateQuantity(newQty)
            }
        )
    }
}


@Composable
fun QuantityUpdateDialog(
    productName: String,
    initialQuantity: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var quantity by remember { mutableStateOf(initialQuantity) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = { onConfirm(quantity) }) {
                Text("Save Changes")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Cancel")
            }
        },
        title = { Text(text = productName) },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = { if (quantity > 0) quantity-- }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Decrease")
                    }
                    Text(
                        text = quantity.toString(),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    IconButton(onClick = { quantity++ }) {
                        Icon(Icons.Default.Add, contentDescription = "Increase")
                    }
                }
                Text("Original: $initialQuantity", style = MaterialTheme.typography.bodyMedium)
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}










