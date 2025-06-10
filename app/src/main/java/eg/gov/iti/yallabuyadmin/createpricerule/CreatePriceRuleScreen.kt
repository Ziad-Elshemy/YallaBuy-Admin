package eg.gov.iti.yallabuyadmin.createpricerule


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import eg.gov.iti.yallabuyadmin.R
import eg.gov.iti.yallabuyadmin.addproduct.DropdownField
import eg.gov.iti.yallabuyadmin.editpricerule.DatePickerField
import eg.gov.iti.yallabuyadmin.editpricerule.toISOString
import eg.gov.iti.yallabuyadmin.model.PriceRulesItem
import eg.gov.iti.yallabuyadmin.model.Response
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreatePriceRuleScreen(
    navController: NavController,
    viewModel: CreatePriceRuleViewModel
) {
    val createState by viewModel.createState.collectAsState()
    val toastHost = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.toastMessage.collect { message ->
            toastHost.showSnackbar(message)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = toastHost) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2CABAB),
                            Color(0xFFE1F5FE)
                        )
                    )
                )
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            CreatePriceRuleForm(
                onSaveClick = { newRule ->
                    viewModel.createPriceRule(newRule)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

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



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreatePriceRuleForm(
    onSaveClick: (PriceRulesItem) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var value by remember { mutableStateOf("") }
    var valueType by remember { mutableStateOf("fixed_amount") }
    var allocationMethod by remember { mutableStateOf("each") }
    var usageLimit by remember { mutableStateOf("0") }
    var oncePerCustomer by remember { mutableStateOf(false) }
    var startsAt by remember { mutableStateOf(LocalDate.now()) }
    var endsAt by remember { mutableStateOf(startsAt.plusDays(7)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val iconRes = if (valueType == "fixed_amount") R.drawable.ic_money else R.drawable.ic_percentage
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterHorizontally)
        )

        OutlinedTextField(title, { title = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value, { value = it }, label = { Text("Value") }, modifier = Modifier.fillMaxWidth())

        DropdownField("Value Type", valueType, listOf("fixed_amount", "percentage")) { valueType = it }
//        DropdownField("Allocation", allocationMethod, listOf("each", "across")) { allocationMethod = it }

        OutlinedTextField(
            usageLimit,
            { usageLimit = it },
            label = { Text("Usage Limit") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Use code once per Customer")
            Spacer(modifier = Modifier.weight(1f))
            Switch(checked = oncePerCustomer, onCheckedChange = { oncePerCustomer = it })
        }

        DatePickerField("Starts At", startsAt) { startsAt = it }
        DatePickerField("Ends At", endsAt) { endsAt = it }

        Button(
            onClick = {
                val newRule = PriceRulesItem(
                    title = title,
                    value = value,
                    valueType = valueType,
                    allocationMethod = "across",
                    usageLimit = usageLimit,
                    oncePerCustomer = oncePerCustomer,
                    startsAt = startsAt.toISOString(),
                    endsAt = endsAt.toISOString(),
                    targetType = "line_item",
                    targetSelection = "all",
                    customerSelection = "all"
                )
                onSaveClick(newRule)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Price Rule")
        }
    }
}










