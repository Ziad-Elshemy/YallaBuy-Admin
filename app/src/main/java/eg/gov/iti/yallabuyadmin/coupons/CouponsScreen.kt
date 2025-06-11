package eg.gov.iti.yallabuyadmin.coupons


import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import eg.gov.iti.yallabuyadmin.R
import eg.gov.iti.yallabuyadmin.model.DiscountCode
import eg.gov.iti.yallabuyadmin.model.PriceRulesItem
import eg.gov.iti.yallabuyadmin.model.Response
import eg.gov.iti.yallabuyadmin.navigation.NavigationRoute
import eg.gov.iti.yallabuyadmin.products.LoadingIndicator


@Composable
fun CouponsScreen(navController: NavController,
                  viewModel: CouponsViewModel,
                  snackBarHostState: SnackbarHostState) {
    val uiState by viewModel.priceRules.collectAsState()
    val uiState2 by viewModel.discountCodes.collectAsState()

    val currentTab = remember { mutableStateOf("rules") } // "rules" or "discounts"

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF009688), Color(0xFFE1F5FE))
    )

    LaunchedEffect(Unit) {
        viewModel.fetchPriceRules()
    }
    LaunchedEffect(currentTab.value) {
        if (currentTab.value == "rules") {
            viewModel.fetchPriceRules()
        } else {
            viewModel.fetchDiscountCodes()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.deleteResult.collect { message ->
            snackBarHostState.showSnackbar(message)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.toastMessage.collect { message ->
            snackBarHostState.showSnackbar(message)
        }
    }


//    Scaffold(
//        modifier = Modifier.fillMaxSize(),
//        snackbarHost = { SnackbarHost(snackBarHostState) }
//    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
//                .background(brush = backgroundBrush)
                .background(Color(0xFFF8F9FA))
//                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Header Row with Add and Search Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    if (currentTab.value == "rules") "Price Rules" else "Discount Codes",
                    style = MaterialTheme.typography.titleLarge
                )
                Row {
                    IconButton(onClick = { /* Search Click */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = {
                        if (currentTab.value == "rules") {
                            navController.navigate(NavigationRoute.CreatePriceRule.route)
                        } else {
                             navController.navigate(NavigationRoute.CreateDiscount.route)
                        }
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                }

            }

            Spacer(modifier = Modifier.height(12.dp))

            // Toggle Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TabButton(
                    text = "Price Rules",
                    selected = currentTab.value == "rules",
                    modifier = Modifier.weight(1f)
                ) {
                    currentTab.value = "rules"
//                    viewModel.fetchPriceRules()
                }

                TabButton(
                    text = "Discounts",
                    selected = currentTab.value == "discounts",
                    modifier = Modifier.weight(1f)
                ) {
                    currentTab.value = "discounts"
                    // viewModel.fetchDiscounts()
                }
            }


            // Main Content
            when(currentTab.value) {
                "rules" -> {
                    when (uiState) {
                        is Response.Loading -> LoadingIndicator()
                        is Response.Failure -> Text("Failed to load", color = Color.Red)
                        is Response.Success -> {
                            val rules = (uiState as Response.Success<List<PriceRulesItem>>).data
                            if (currentTab.value == "rules") {
                                PriceRulesScreen(
                                    rules = rules,
                                    navController = navController,
                                    onEditClick = { rule ->
                                        navController.currentBackStackEntry?.savedStateHandle?.set("selected_rule", rule)
                                        navController.navigate(NavigationRoute.EditPriceRule.route)
                                    }
                                ) { rule ->
                                    Log.e("CouponsScreen", "Rule deleted: ${rule.title}", )
                                    val priceRuleId = rule.id ?: return@PriceRulesScreen
                                    viewModel.deletePriceRule(priceRuleId)
                                }
                            } else {
                                Text("Discounts will be shown here")
                            }
                        }
                    }
                }
                "discounts" -> {
                    when (uiState2) {
                        is Response.Loading -> LoadingIndicator()
                        is Response.Success -> {
                            val discounts = (uiState2 as Response.Success<List<DiscountCode>>).data
                            DiscountsScreen(
                                discounts = discounts,
                                onEditClick = { discount,newCode ->
                                    val priceRuleId = discount.priceRuleId ?: return@DiscountsScreen
                                    val discountId = discount.id ?: return@DiscountsScreen
                                    viewModel.updateDiscountCode(priceRuleId, discountId, newCode)
                                }
                            ){ discount ->
                                val priceRuleId = discount.priceRuleId ?: return@DiscountsScreen
                                val discountId = discount.id ?: return@DiscountsScreen
                                viewModel.deleteDiscountCode(priceRuleId, discountId)
                            }
                        }
                        is Response.Failure -> Text("Error loading discount codes")
                    }
                }
            }

        }
//    }
}

@Composable
fun TabButton(text: String, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = modifier
            .height(48.dp)
            .background(if (selected) Color(0xFF009688) else Color.Transparent)
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else Color.Black,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}





@Composable
fun PriceRulesScreen(
    rules: List<PriceRulesItem>,
    navController: NavController,
    onEditClick: (PriceRulesItem) -> Unit,
    onDeleteClick: (PriceRulesItem) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        LazyColumn {
            items(rules) { rule ->
                PriceRuleItemCard(
                    rule = rule,
                    onEditClick = onEditClick,
                    onDelete = { onDeleteClick(rule) })
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun PriceRuleItemCard(
    rule: PriceRulesItem,
    onEditClick: (PriceRulesItem) -> Unit,
    onDelete: () -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Delete Price Rule") },
            text = { Text("Are you sure you want to delete the rule: ${rule.title}?") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    onDelete()
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEditClick(rule) }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon based on type
            val iconRes = if (rule.valueType == "fixed_amount") R.drawable.ic_money else R.drawable.ic_percentage
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(rule.title ?: "No Rule Title", fontWeight = FontWeight.Bold)
                Text("Value: ${rule.value}")
                Text("Expiry Date: ${rule.endsAt ?: "None"}")
            }

            IconButton(onClick = { showDialog = true}) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Rule", tint = Color.Red)
            }
        }
    }
}


@Composable
fun DiscountsScreen(
    discounts: List<DiscountCode>,
    onEditClick: (DiscountCode,String) -> Unit,
    onDeleteClick: (DiscountCode) -> Unit
) {
    LazyColumn {
        items(discounts) { discount ->
            DiscountItemCard(
                discount = discount,
                onEditClick = { newCode->
                    onEditClick(discount,newCode)
                              },
                onDelete = { onDeleteClick(discount) }
            )
        }
    }
}

@Composable
fun DiscountItemCard(
    discount: DiscountCode,
    onEditClick: (String) -> Unit,
    onDelete: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Delete Discount Code") },
            text = { Text("Are you sure you want to delete the code: ${discount.code}?") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    onDelete()
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }


    if (showEditDialog) {
        EditDiscountDialog(
            currentCode = discount.code ?: "",
            onDismiss = { showEditDialog = false },
            onConfirm = { newCode ->
                onEditClick(newCode)
                showEditDialog = false
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { showEditDialog = true },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Code: ${discount.code}", fontWeight = FontWeight.Bold)
                Text("Usage: ${discount.usageCount ?: 0}")
            }

            IconButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Discount", tint = Color.Red)
            }
        }
    }
}


@Composable
fun EditDiscountDialog(
    currentCode: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var updatedCode by remember { mutableStateOf(currentCode) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Discount Code") },
        text = {
            OutlinedTextField(
                value = updatedCode,
                onValueChange = { updatedCode = it },
                label = { Text("Discount Code") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(
                updatedCode
            ) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}












