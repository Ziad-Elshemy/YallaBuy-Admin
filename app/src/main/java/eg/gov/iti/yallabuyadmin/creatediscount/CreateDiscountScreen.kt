package eg.gov.iti.yallabuyadmin.creatediscount


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import eg.gov.iti.yallabuyadmin.addproduct.DropdownField
import eg.gov.iti.yallabuyadmin.model.DiscountCode
import eg.gov.iti.yallabuyadmin.model.PriceRulesItem
import eg.gov.iti.yallabuyadmin.model.Response
import eg.gov.iti.yallabuyadmin.products.LoadingIndicator
import kotlinx.coroutines.launch


@Composable
fun CreateDiscountScreen(
    navController: NavController,
    viewModel: CreateDiscountViewModel
) {
    val createState by viewModel.createState.collectAsState()
    val uiState by viewModel.priceRules.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedRule by remember { mutableStateOf<PriceRulesItem?>(null) }
    var discountCode by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.fetchPriceRules()
    }

    LaunchedEffect(Unit) {
        viewModel.toastMessage.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Create New Discount", style = MaterialTheme.typography.headlineSmall)

            val coroutineScope = rememberCoroutineScope()

            when (uiState) {
                is Response.Loading -> LoadingIndicator()
                is Response.Failure -> Text("Failed to load rules", color = Color.Red)
                is Response.Success -> {
                    val rules = (uiState as Response.Success<List<PriceRulesItem>>).data

                    // Dropdown for selecting Price Rule
                    DropdownField(
                        label = "Select Price Rule",
                        selected = selectedRule?.title ?: "Choose...",
                        options = rules.mapNotNull { it.title }
                    ) { selectedTitle ->
                        selectedRule = rules.firstOrNull { it.title == selectedTitle }
                    }

                    OutlinedTextField(
                        value = discountCode,
                        onValueChange = { discountCode = it },
                        label = { Text("Discount Code") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            val ruleId = selectedRule?.id
                            if (ruleId != null && discountCode.isNotBlank()) {
                                val newDiscountCode = DiscountCode(
                                    code = discountCode
                                )
                                viewModel.createDiscountCode(ruleId, newDiscountCode)
                            } else {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Please select a rule and enter a code.")
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Create Discount")
                    }
                }
            }

            when (createState) {
                is Response.Loading -> { /* show loader if needed */ }
                is Response.Success -> {
                    LaunchedEffect(Unit) {
                        navController.popBackStack()
                    }
                }
                is Response.Failure -> {
                    Text(
                        text = "Error: ${(createState as Response.Failure).error.message}",
                        color = Color.Red
                    )
                }
            }

        }
    }
}










