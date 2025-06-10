package eg.gov.iti.yallabuyadmin.products


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skydoves.landscapist.glide.GlideImage
import eg.gov.iti.yallabuyadmin.model.ProductsItem
import eg.gov.iti.yallabuyadmin.model.Response
import eg.gov.iti.yallabuyadmin.navigation.NavigationRoute


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    navController: NavController,
    viewModel: ProductsViewModel,
    snackBarHostState: SnackbarHostState
) {


    viewModel.fetchProductsItems()

    val uiState by viewModel.allProducts.collectAsStateWithLifecycle()

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
                "Products",
                style = MaterialTheme.typography.titleLarge
            )
            Row {
                IconButton(onClick = { /* Search Click */ }) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
                IconButton(onClick = {
                    navController.navigate(NavigationRoute.CreateProduct.route)
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }

        }

        Spacer(modifier = Modifier.height(12.dp))

        when (uiState) {
            is Response.Loading -> {
                LoadingIndicator()
            }

            is Response.Success -> {
                ProductsScreenUI(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    navController = navController,
                    viewModel,
                    (uiState as Response.Success<List<ProductsItem?>?>).data
                )
            }

            is Response.Failure -> {
                Text(
                    text = "Failed to fetch data",
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(),
                    fontSize = 22.sp
                )
            }


        }

        LaunchedEffect(key1 = viewModel.toastMessage) {
            viewModel.toastMessage.collect { message ->
                if (!message.isNullOrBlank()) {
                    snackBarHostState.showSnackbar(
                        message = message,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }

    }
//    }
}


@Composable
fun ProductsScreenUI(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ProductsViewModel,
    products: List<ProductsItem?>?
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProductsList(
            products = products,
            navController = navController,
            onDeleteProduct = { product ->
                viewModel.deleteProductById(product?.id ?: 0)
                // add snack bar
            }
        )
    }
}

@Composable
fun ProductsList(
    products: List<ProductsItem?>?,
    navController: NavController,
    onDeleteProduct: (ProductsItem?) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        items(products ?: emptyList()) { product ->
            ProductItem(
                product = product,
                navController = navController,
                onDeleteClick = { onDeleteProduct(product) }
            )
        }
    }
}

@Composable
fun ProductItem(
    product: ProductsItem?,
    navController: NavController,
    onDeleteClick: () -> Unit
) {
    val imageUrl = product?.images?.firstOrNull()?.src
    val price = product?.variants?.firstOrNull()?.price ?: "0.00"
    val quantity = product?.variants?.firstOrNull()?.inventoryQuantity ?: 0


    Box(modifier = Modifier.padding(4.dp)) {
        Log.e("ProductItem", "ProductItem: id = ${product?.id}")
        Card(
            modifier = Modifier
                .clickable {
                    Log.e("ProductItem", "ProductItem: clickable id = ${product?.id}")
                    navController.navigate(
                        NavigationRoute.ProductDetails.createRoute(
                            product?.id ?: 11916346917182
                        )
                    )
                }
                .width(220.dp)
                .height(240.dp)
                .padding(4.dp),
            colors = CardColors(
                containerColor = Color.White,
                contentColor = Color.Black,
                disabledContentColor = Color.White,
                disabledContainerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    if (!imageUrl.isNullOrEmpty()) {
                        GlideImage(
                            imageModel = { imageUrl },
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(10.dp)),
                            loading = {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                            },
                            failure = {
                                Icon(Icons.Default.Build, contentDescription = "Failed to load")
                            }
                        )
                    } else {
                        Icon(Icons.Default.Search, contentDescription = "No image")
                    }

                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = product?.productType?.uppercase() ?: "NO CATEGORY",
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Gray
                )

                Text(
                    text = product?.title?.uppercase() ?: "NO TITLE",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Text(
                    text = "$$price",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )


                Text(
                    text = when {
                        quantity == 0 -> "Out of stock"
                        quantity <= 5 -> "⚠Low stock ($quantity)"
                        else -> "In stock ($quantity)"
                    },
                    color = when {
                        quantity == 0 -> Color.Red
                        quantity <= 5 -> Color(0xFFFFA500)
                        else -> Color(0xFF4CAF50)
                    },
                    fontWeight = FontWeight.SemiBold
                )


            }
        }

        Box(
            modifier = Modifier
                .padding(16.dp)
                .size(40.dp)
                .offset(x = 2.dp, y = 2.dp)
                .shadow(
                    elevation = 6.dp,
                    shape = CircleShape,
                    clip = false
                )
                .clip(CircleShape)
                .background(Color.White)
                .align(Alignment.TopEnd)
                .clickable(onClick = onDeleteClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.Red,
                modifier = Modifier.size(20.dp)
            )
        }

    }

}


@Composable
fun LoadingIndicator() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize()
    ) {
        CircularProgressIndicator()
    }

}







