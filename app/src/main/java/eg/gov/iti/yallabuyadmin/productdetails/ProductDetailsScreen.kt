package eg.gov.iti.yallabuyadmin.productdetails


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.skydoves.landscapist.glide.GlideImage
import eg.gov.iti.yallabuyadmin.model.ProductsItem
import eg.gov.iti.yallabuyadmin.model.Response
import eg.gov.iti.yallabuyadmin.model.UpdateProductRequest
import eg.gov.iti.yallabuyadmin.model.VariantsItem
import eg.gov.iti.yallabuyadmin.products.LoadingIndicator
import eg.gov.iti.yallabuyadmin.products.ProductsScreenUI


@Composable
fun ProductDetailsScreen(navController: NavController,
                         viewModel: ProductDetailsViewModel,
                         productId: Long) {

    Log.e("ProductDetailsScreen", "ProductDetailsScreen: productId = $productId", )

    LaunchedEffect(Unit) {
        viewModel.fetchProductById(productId)
    }

    val uiState by viewModel.productDetails.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF2CABAB),
            Color(0xFFE1F5FE)
        )
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackBarHostState) }
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

            when (uiState) {
                is Response.Loading -> {
                    LoadingIndicator()
                }

                is Response.Success -> {
                    ProductDetailsScreenUI(
                        product = (uiState as Response.Success<ProductsItem?>).data,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ){ updatedProduct ->
                        viewModel.updateProductDetails(
                            productId = productId,
                            body = UpdateProductRequest(
                                product = updatedProduct
                            ))

//                        navController.popBackStack()
                    }
                    Log.e("TAG", "ProductDetailsScreen: ${(uiState as Response.Success<ProductsItem?>).data}", )
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
                viewModel.toastMessage.collect{message->
                    if (!message.isNullOrBlank()){
                        snackBarHostState.showSnackbar(
                            message = message,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }

        }
    }
}


//@Composable
//fun ProductDetailsScreenUI(
//    modifier: Modifier,
//    product: ProductsItem?,
//    onUpdateClick: (ProductsItem) -> Unit
//) {
//    var title by remember { mutableStateOf(product?.title ?: "") }
//    var price by remember { mutableStateOf(product?.variants?.firstOrNull()?.price ?: "") }
//    var quantity by remember { mutableStateOf(product?.variants?.firstOrNull()?.inventoryQuantity?.toString() ?: "") }
//    var productType by remember { mutableStateOf(product?.productType ?: "") }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//            .verticalScroll(rememberScrollState()),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//
//        if (!product?.image?.src.isNullOrEmpty()) {
//            GlideImage(
//                imageModel = { product!!.image!!.src },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(200.dp)
//                    .clip(RoundedCornerShape(12.dp)),
//                loading = {
//                    CircularProgressIndicator(modifier = Modifier.size(32.dp))
//                },
//                failure = {
//                    Icon(Icons.Default.Build, contentDescription = null)
//                }
//            )
//        }
//
//        // name
//        OutlinedTextField(
//            value = title,
//            onValueChange = { title = it },
//            label = { Text("Product Title") },
//            singleLine = true,
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        // price
//        OutlinedTextField(
//            value = price,
//            onValueChange = { price = it },
//            label = { Text("Price") },
//            singleLine = true,
//            modifier = Modifier.fillMaxWidth(),
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//        )
//
//        // Quantity
//        OutlinedTextField(
//            value = quantity,
//            onValueChange = { quantity = it },
//            label = { Text("Quantity") },
//            singleLine = true,
//            modifier = Modifier.fillMaxWidth(),
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//        )
//
//        // Type
//        OutlinedTextField(
//            value = productType,
//            onValueChange = { productType = it },
//            label = { Text("Product Type") },
//            singleLine = true,
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        // Update Button
//        Button(
//            onClick = {
//                val updatedProduct = product?.copy(
//                    title = title,
//                    productType = productType,
//                    variants = listOf(
//                        product.variants?.firstOrNull()?.copy(
//                            price = price,
//                            inventoryQuantity = quantity.toIntOrNull() ?: 0
//                        ) ?: VariantsItem()
//                    )
//                )
//                updatedProduct?.let { onUpdateClick(it) }
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(48.dp),
//            shape = RoundedCornerShape(8.dp)
//        ) {
//            Text("Update Product")
//        }
//    }
//}

@Composable
fun ProductDetailsScreenUI(
    modifier: Modifier,
    product: ProductsItem? ,
    onUpdateClick: (ProductsItem) -> Unit
) {
    var title by remember { mutableStateOf(product?.title ?: "") }
    var price by remember { mutableStateOf(product?.variants?.firstOrNull()?.price ?: "") }
    var quantity by remember { mutableStateOf(product?.variants?.firstOrNull()?.inventoryQuantity?.toString() ?: "") }
    var productType by remember { mutableStateOf(product?.productType ?: "") }
    var productImages by remember { mutableStateOf(product?.images ?: emptyList()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        LazyRow {
            items(productImages){ imgUrl ->

                ProductImageItem(imgUrl?.src ?: "")

            }
        }

        // name
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Product Title") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // price
        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        // Quantity
        OutlinedTextField(
            value = quantity,
            onValueChange = { quantity = it },
            label = { Text("Quantity") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        // Type
        OutlinedTextField(
            value = productType,
            onValueChange = { productType = it },
            label = { Text("Product Type") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Update Button
        Button(
            onClick = {
                val updatedProduct = product?.copy(
                    title = title,
                    productType = productType,
                    variants = listOf(
                        product.variants?.firstOrNull()?.copy(
                            price = price,
                            inventoryQuantity = quantity.toIntOrNull() ?: 0
                        ) ?: VariantsItem()
                    )
                )
                updatedProduct?.let { onUpdateClick(it) }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Update Product")
        }

    }
}


@Composable
fun ProductImageItem(imgUrl: String) {

    Box (modifier = Modifier.padding(4.dp)){
        Log.e("ProductItem", "ProductItem: id = ", )
        Card(
            modifier = Modifier
//                .clickable {
//                    Log.e("ProductItem", "ProductItem: clickable id = ${product?.id}", )
//                    navController.navigate(
//                        NavigationRoute.ProductDetails.createRoute(
//                            product?.id ?: 11916346917182
//                        )
//                    )
//                }
                .width(180.dp)
                .height(220.dp)
                .padding(4.dp),
            colors = CardColors(containerColor = Color.White,
                contentColor = Color.Black,
                disabledContentColor = Color.White,
                disabledContainerColor = Color.White),
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
                        .height(160.dp)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    if (!imgUrl.isNullOrEmpty()) {
                        GlideImage(
                            imageModel = { imgUrl },
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
                .align(Alignment.TopEnd),
//                .clickable(onClick = onDeleteClick),
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







