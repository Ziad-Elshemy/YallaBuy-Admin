package eg.gov.iti.yallabuyadmin.addproduct

import android.util.Log
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import eg.gov.iti.yallabuyadmin.model.Image
import eg.gov.iti.yallabuyadmin.model.ImagesItem
import eg.gov.iti.yallabuyadmin.model.OptionsItem
import eg.gov.iti.yallabuyadmin.model.ProductsItem
import eg.gov.iti.yallabuyadmin.model.Response
import eg.gov.iti.yallabuyadmin.model.VariantsItem
import eg.gov.iti.yallabuyadmin.products.LoadingIndicator

@Composable
fun CreateProductScreen(
    navController: NavController,
    viewModel: CreateProductViewModel
){

//    Log.e("ProductDetailsScreen", "ProductDetailsScreen: productId = $productId", )

    LaunchedEffect(Unit) {
//        viewModel.fetchProductById(productId)
    }

//    val uiState by viewModel.productDetails.collectAsStateWithLifecycle()
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

//            when (uiState) {
//                is Response.Loading -> {
//                    LoadingIndicator()
//                }
//
//                is Response.Success -> {

                    CreateProductUI(
                        onSubmit = {},
                        vendors = listOf("vendor1","vendor2"),
                        productTypes = listOf("type1","type2", "type3")
                    )

//                }
//
//                is Response.Failure -> {
//                    Text(
//                        text = "Failed to fetch data",
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .wrapContentSize(),
//                        fontSize = 22.sp
//                    )
//                }
//
//
//            }
//
//            LaunchedEffect(key1 = viewModel.toastMessage) {
//                viewModel.toastMessage.collect { message ->
//                    if (!message.isNullOrBlank()) {
//                        snackBarHostState.showSnackbar(
//                            message = message,
//                            duration = SnackbarDuration.Short
//                        )
//                    }
//                }
//            }
        }
    }
}


@Composable
fun CreateProductUI(
    onSubmit: (ProductsItem) -> Unit,
    vendors: List<String>,
    productTypes: List<String>
) {
    val imageList = remember { mutableStateListOf<LocalImageItem>() }
    var currentImageUrl by remember { mutableStateOf("") }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedVendor by remember { mutableStateOf(vendors.firstOrNull() ?: "") }
    var selectedType by remember { mutableStateOf(productTypes.firstOrNull() ?: "") }
    var tags by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("active") }

    var price by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var sku by remember { mutableStateOf("") }

    // Options and Variants
    val sizes = remember { mutableStateListOf<String>() }
    val colors = remember { mutableStateListOf<String>() }
    var currentSize by remember { mutableStateOf("") }
    var currentColor by remember { mutableStateOf("") }

    Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(16.dp)) {
        OutlinedTextField(title, { title = it }, label = { Text("Title") })
        OutlinedTextField(description, { description = it }, label = { Text("Description") })

        // Dropdown for Vendor
        DropdownField("Vendor", selectedVendor, vendors) { selectedVendor = it }
        DropdownField("Product Type", selectedType, productTypes) { selectedType = it }

        OutlinedTextField(tags, { tags = it }, label = { Text("Tags") })

        Divider(Modifier.padding(vertical = 8.dp))

        // Images section
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = currentImageUrl,
                onValueChange = { currentImageUrl = it },
                label = { Text("Image URL") },
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = {
                if (currentImageUrl.isNotBlank()) {
                    imageList.add(LocalImageItem(currentImageUrl))
                    currentImageUrl = ""
                }
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Image")
            }
        }

        LazyRow {
            items(imageList) { image ->
                AsyncImage(
                    model = image.url,
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }

        Divider(Modifier.padding(vertical = 8.dp))

        // Options inputs
        Row {
            OutlinedTextField(currentSize, { currentSize = it }, label = { Text("Size") })
            Button(onClick = {
                if (currentSize.isNotBlank()) {
                    sizes.add(currentSize)
                    currentSize = ""
                }
            }) { Text("Add Size") }
        }

        Row {
            OutlinedTextField(currentColor, { currentColor = it }, label = { Text("Color") })
            Button(onClick = {
                if (currentColor.isNotBlank()) {
                    colors.add(currentColor)
                    currentColor = ""
                }
            }) { Text("Add Color") }
        }

        // Variant inputs
        OutlinedTextField(price, { price = it }, label = { Text("Price") })
        OutlinedTextField(quantity, { quantity = it }, label = { Text("Inventory Quantity") })
        OutlinedTextField(sku, { sku = it }, label = { Text("SKU") })

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            val product = ProductsItem(
                title = title,
                bodyHtml = description,
                vendor = selectedVendor,
                productType = selectedType,
                tags = tags,
                status = status,
                image = imageList.firstOrNull()?.let { Image(src = it.url) },
                images = imageList.map { ImagesItem(src = it.url) },
                options = listOf(
                    OptionsItem(name = "Size", values = sizes),
                    OptionsItem(name = "Color", values = colors)
                ),
                variants = listOf(
                    VariantsItem(
                        price = price,
                        inventoryQuantity = quantity.toIntOrNull() ?: 0,
                        sku = sku,
                        option1 = sizes.firstOrNull(),
                        option2 = colors.firstOrNull()
                    )
                )
            )
            onSubmit(product)
        }) {
            Text("Create Product")
        }
    }
}

@Composable
fun DropdownField(label: String, selected: String, options: List<String>, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown", Modifier.clickable { expanded = true })
            },
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach {
                DropdownMenuItem(onClick = {
                    onSelected(it)
                    expanded = false
                }, text = { Text(it) })
            }
        }
    }
}
