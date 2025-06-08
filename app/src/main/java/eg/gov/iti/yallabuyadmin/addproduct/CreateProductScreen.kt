package eg.gov.iti.yallabuyadmin.addproduct

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.flowlayout.FlowRow
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

    val vendorsState by viewModel.vendors.collectAsStateWithLifecycle()
    val productTypesState by viewModel.productTypes.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.loadInitialData()
    }

    val uiState by viewModel.createState.collectAsStateWithLifecycle()
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



            when {
                vendorsState is Response.Loading || productTypesState is Response.Loading -> {
                    LoadingIndicator()
                }

                vendorsState is Response.Failure || productTypesState is Response.Failure -> {

                    Text(text = "Failed to create product",
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(),
                        fontSize = 22.sp,
                        color = Color.Red)

                }

                vendorsState is Response.Success && productTypesState is Response.Success -> {
                    CreateProductUI(
                        vendors = (vendorsState as Response.Success<List<String>>).data,
                        productTypes = (productTypesState as Response.Success<List<String>>).data,
                        onSubmit = {product ->
                            viewModel.createProduct(product)
                        }
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
//    var tags by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("active") }

//    var price by remember { mutableStateOf("") }
//    var quantity by remember { mutableStateOf("") }
//    var sku by remember { mutableStateOf("") }
//
//    // Options and Variants
//    val sizes = remember { mutableStateListOf<String>() }
//    val colors = remember { mutableStateListOf<String>() }
//    var currentSize by remember { mutableStateOf("") }
//    var currentColor by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .verticalScroll(rememberScrollState())
        .padding(16.dp)) {

        // Images section

        LazyRow {
            items(imageList) { image ->
                AsyncImage(
                    model = image.url,
                    contentDescription = null,
                    modifier = Modifier
                        .width(140.dp)
                        .height(180.dp)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }

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




        Divider(Modifier.padding(vertical = 8.dp))

        OutlinedTextField(title, { title = it }, label = { Text("Title") })
        OutlinedTextField(description, { description = it }, label = { Text("Description") })

        // Dropdown for Vendor
        DropdownField("Vendor", selectedVendor, vendors) { selectedVendor = it }
        DropdownField("Product Type", selectedType, productTypes) { selectedType = it }

//        OutlinedTextField(tags, { tags = it }, label = { Text("Tags") })

        Divider(Modifier.padding(vertical = 8.dp))


        // Options structure
        val optionNames = remember { mutableStateListOf<String>() }
        var newOptionName by remember { mutableStateOf("") }
        val optionValues = remember { mutableStateMapOf<String, MutableList<String>>() }

// Add option
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = newOptionName,
                onValueChange = { newOptionName = it },
                label = { Text("Add Option (max 3)") },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = {
                    if (newOptionName.isNotBlank() && optionNames.size < 3 && !optionNames.contains(newOptionName)) {
                        optionNames.add(newOptionName)
                        optionValues[newOptionName] = mutableListOf()
                        newOptionName = ""
                    }
                }
            ) { Text("Add") }
        }

        Spacer(Modifier.height(12.dp))

// For each option, add values
        optionNames.forEach { option ->
            var newValue by remember { mutableStateOf("") }

            Text(option, style = MaterialTheme.typography.labelLarge)
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = newValue,
                    onValueChange = { newValue = it },
                    label = { Text("Add value to $option") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                Button(onClick = {
                    if (newValue.isNotBlank()) {
                        optionValues[option]?.add(newValue)
                        newValue = ""
                    }
                }) { Text("Add") }
            }

            // Show values added
            FlowRow(modifier = Modifier.fillMaxWidth(), mainAxisSpacing = 8.dp) {
                optionValues[option]?.forEach {
                    Box(
                        modifier = Modifier
                            .background(Color.LightGray, RoundedCornerShape(50))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(it)
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
        }


        // Choose one value from each option
        val selectedValues = remember { mutableStateMapOf<String, String>() }
        var variantPrice by remember { mutableStateOf("") }
        var variantQuantity by remember { mutableStateOf("") }
//        var variantSku by remember { mutableStateOf("") }
        val variantsList = remember { mutableStateListOf<VariantsItem>() }


        Text("Add Variant", fontWeight = FontWeight.Bold, fontSize = 18.sp)

        optionNames.forEach { option ->
            DropdownField(
                label = option,
                selected = selectedValues[option] ?: "",
                options = optionValues[option] ?: emptyList()
            ) {
                selectedValues[option] = it
            }
        }

        Spacer(Modifier.height(8.dp))
        OutlinedTextField(variantPrice, { variantPrice = it }, label = { Text("Price") })
        OutlinedTextField(variantQuantity, { variantQuantity = it }, label = { Text("Quantity") })
//        OutlinedTextField(variantSku, { variantSku = it }, label = { Text("SKU") })

        Button(
            onClick = {
                if (selectedValues.size == optionNames.size && variantPrice.isNotBlank()) {
                    val variant = VariantsItem(
                        price = variantPrice,
                        inventoryQuantity = variantQuantity.toIntOrNull() ?: 0,
//                        sku = variantSku,
                        option1 = selectedValues[optionNames.getOrNull(0)],
                        option2 = optionNames.getOrNull(1)?.let { selectedValues[it] },
                        option3 = optionNames.getOrNull(2)?.let { selectedValues[it] }
                    )
                    variantsList.add(variant)
                    // Reset input
                    variantPrice = ""
                    variantQuantity = ""
//                    variantSku = ""
                    selectedValues.clear()
                }
            }
        ) {
            Text("Add Variant")
        }



        Spacer(Modifier.height(16.dp))

        Button(onClick = {

            val product = ProductsItem(
                title = title,
                bodyHtml = description,
                vendor = selectedVendor,
                productType = selectedType,
//                tags = tags,
                status = status,
                image = imageList.firstOrNull()?.let { Image(src = it.url) },
                images = imageList.map { ImagesItem(src = it.url) },
                options = optionNames.mapIndexed { index, name ->
                    OptionsItem(name = name, values = optionValues[name], position = index + 1)
                },
                variants = variantsList
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
