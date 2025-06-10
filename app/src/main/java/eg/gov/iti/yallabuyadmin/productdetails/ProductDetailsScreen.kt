package eg.gov.iti.yallabuyadmin.productdetails


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.skydoves.landscapist.glide.GlideImage
import eg.gov.iti.yallabuyadmin.addproduct.DropdownField
import eg.gov.iti.yallabuyadmin.model.AddImageRequest
import eg.gov.iti.yallabuyadmin.model.ImagesItem
import eg.gov.iti.yallabuyadmin.model.OptionsItem
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
    LaunchedEffect(Unit) {
        viewModel.loadInitialData()
    }

    val vendorsState by viewModel.vendors.collectAsStateWithLifecycle()
    val productTypesState by viewModel.productTypes.collectAsStateWithLifecycle()
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
                .background(Color(0xFFF8F9FA))
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            when  {
                vendorsState is Response.Loading || productTypesState is Response.Loading || uiState is Response.Loading -> {
                    LoadingIndicator()
                }

                vendorsState is Response.Failure || productTypesState is Response.Failure || uiState is Response.Failure -> {
                    Text(
                        text = "Failed to fetch data",
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(),
                        fontSize = 22.sp
                    )
                }

                vendorsState is Response.Success && productTypesState is Response.Success && uiState is Response.Success -> {

                    ProductDetailsScreenUI(
                        product = (uiState as Response.Success<ProductsItem?>).data,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                        ,
                        vendors = (vendorsState as Response.Success<List<String>>).data,
                        productTypes = (productTypesState as Response.Success<List<String>>).data,
                        onAddNewImageClick = { imageRequest ->
                            viewModel.addImageToProduct(
                                productId = productId,
                                body = AddImageRequest(
                                    image = imageRequest
                                )
                            )
                        },
                        onDeleteClick = { imgId ->
                            viewModel.deleteProductImage(productId,imgId)
                        }
                    )
                    { updatedProduct ->
                        viewModel.updateProductDetails(
                            productId = productId,
                            body = UpdateProductRequest(
                                product = updatedProduct
                            ))

//                        navController.popBackStack()
                    }
                    Log.e("TAG", "ProductDetailsScreen: ${(uiState as Response.Success<ProductsItem?>).data}", )
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


@Composable
fun ProductDetailsScreenUI(
    modifier: Modifier,
    product: ProductsItem?,
    vendors: List<String>,
    productTypes: List<String>,
    onAddNewImageClick: (ImagesItem) -> Unit,
    onDeleteClick: (Long) -> Unit,
    onUpdateClick: (ProductsItem) -> Unit
) {
    var newImgUrl by remember { mutableStateOf("") }
    var title by remember { mutableStateOf(product?.title ?: "") }
    var description by remember { mutableStateOf(product?.bodyHtml ?: "") }
    var productType by remember { mutableStateOf(product?.productType ?: "") }
    var selectedVendor by remember { mutableStateOf(product?.vendor ?: vendors.firstOrNull() ?: "") }
    var selectedType by remember { mutableStateOf(product?.productType ?: productTypes.firstOrNull() ?: "") }

    val existingOptions = remember { mutableStateListOf<OptionsItem>() }
    LaunchedEffect(product) {
        if (existingOptions.size != product?.options?.size){
            product?.options?.filterNotNull()?.let { existingOptions.addAll(it) }
        }
    }

    val variantInputs = remember {
        mutableStateListOf<Triple<List<String>, MutableState<String>, MutableState<String>>>()
    }

    val sizes = existingOptions.getOrNull(0)?.values?.filterNotNull() ?: emptyList()
    val colors = existingOptions.getOrNull(1)?.values?.filterNotNull() ?: emptyList()
    val materials = existingOptions.getOrNull(2)?.values?.filterNotNull() ?: emptyList()

    val variantCombinations = remember(sizes, colors, materials) {
        val result = mutableListOf<List<String>>()
        if (colors.isEmpty() && materials.isEmpty()) {
            sizes.forEach { size -> result.add(listOf(size)) }
        } else if (materials.isEmpty()) {
            sizes.forEach { size ->
                colors.forEach { color -> result.add(listOf(size, color)) }
            }
        } else {
            sizes.forEach { size ->
                colors.forEach { color ->
                    materials.forEach { material -> result.add(listOf(size, color, material)) }
                }
            }
        }
        result
    }

    LaunchedEffect(variantCombinations) {
        variantInputs.clear()
        variantCombinations.forEach { combination ->
            variantInputs.add(Triple(
                combination,
                mutableStateOf(product?.variants?.firstOrNull {
                    it?.option1 == combination.getOrNull(0) &&
                            it?.option2 == combination.getOrNull(1) &&
                            it?.option3 == combination.getOrNull(2)
                }?.price ?: ""),
                mutableStateOf(product?.variants?.firstOrNull {
                    it?.option1 == combination.getOrNull(0) &&
                            it?.option2 == combination.getOrNull(1) &&
                            it?.option3 == combination.getOrNull(2)
                }?.inventoryQuantity?.toString() ?: "0")
            ))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LazyRow {
            items(product?.images ?: emptyList()) { imageItem ->
                imageItem?.let {
                    ProductImageItem(imgItem = it, onDeleteClick = onDeleteClick)
                }
            }
        }

        Row {
            OutlinedTextField(
                value = newImgUrl,
                onValueChange = { newImgUrl = it },
                label = { Text("Add New Image URL") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = {
                if (newImgUrl.isNotBlank()) {
                    onAddNewImageClick(ImagesItem(src = newImgUrl))
                    newImgUrl = ""
                }
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Image")
            }
        }

        OutlinedTextField(title, { title = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(description, { description = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth())
        DropdownField("Vendor", selectedVendor, vendors) { selectedVendor = it }
        DropdownField("Product Type", selectedType, productTypes) { selectedType = it }

        Divider()

        Text("Product Options", style = MaterialTheme.typography.titleMedium)
        existingOptions.forEachIndexed { index, option ->
            Column {
                Text("Option ${index + 1}: ${option.name}", fontWeight = FontWeight.Bold)
                option.values?.forEach { Text("- $it", fontSize = 14.sp) }

                var newValue by remember { mutableStateOf("") }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(newValue, { newValue = it }, label = { Text("Add Value") }, modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        if (newValue.isNotBlank()) {
                            val updated = option.values?.toMutableList() ?: mutableListOf()
                            updated.add(newValue)
                            existingOptions[index] = option.copy(values = updated)
                            newValue = ""
                        }
                    }) { Icon(Icons.Default.Add, contentDescription = "Add Value") }
                }
            }
        }

        if (existingOptions.size < 3) {
            var newOptionName by remember { mutableStateOf("") }
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(newOptionName, { newOptionName = it }, label = { Text("Add Option") }, modifier = Modifier.weight(1f))
                IconButton(onClick = {
                    if (newOptionName.isNotBlank()) {
                        existingOptions.add(OptionsItem(name = newOptionName, values = emptyList()))
                        newOptionName = ""
                    }
                }) { Icon(Icons.Default.Add, contentDescription = "Add Option") }
            }
        }

        Divider()
        Text("Define Variants", style = MaterialTheme.typography.titleMedium)

        variantInputs.forEach { (combination, priceState, quantityState) ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Text("Variant: ${combination.joinToString(" / ")}")
                OutlinedTextField(priceState.value, { priceState.value = it }, label = { Text("Price") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                OutlinedTextField(quantityState.value, { quantityState.value = it }, label = { Text("Quantity") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            }
        }

        Button(
            onClick = {
                val variants = variantInputs.map { (combination, priceState, quantityState) ->
                    VariantsItem(
                        option1 = combination.getOrNull(0),
                        option2 = combination.getOrNull(1),
                        option3 = combination.getOrNull(2),
                        price = priceState.value,
                        inventoryManagement = "shopify",
                        inventoryQuantity = quantityState.value.toIntOrNull() ?: 0
                    )
                }

                val updatedProduct = product?.copy(
                    title = title,
                    bodyHtml = description,
                    vendor = selectedVendor,
                    productType = selectedType,
                    options = existingOptions,
                    variants = variants
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
fun ProductImageItem(
    imgItem: ImagesItem,
    onDeleteClick: (Long) -> Unit
) {

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
                    if (!imgItem.src.isNullOrEmpty()) {
                        GlideImage(
                            imageModel = { imgItem.src },
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
                .align(Alignment.TopEnd)
                .clickable(onClick = {
                    if (imgItem.id != null) {
                        onDeleteClick(imgItem.id)
                    }
                }),
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







