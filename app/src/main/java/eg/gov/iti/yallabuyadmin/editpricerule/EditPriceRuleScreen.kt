package eg.gov.iti.yallabuyadmin.editpricerule


import android.app.DatePickerDialog
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import eg.gov.iti.yallabuyadmin.R
import eg.gov.iti.yallabuyadmin.addproduct.DropdownField
import eg.gov.iti.yallabuyadmin.model.PriceRulesItem
import eg.gov.iti.yallabuyadmin.model.Response
import eg.gov.iti.yallabuyadmin.products.LoadingIndicator
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditPriceRuleScreen(
    navController: NavController,
    viewModel: EditPriceRuleViewModel,
    priceRule: PriceRulesItem
) {
    val updateState by viewModel.updateState.collectAsState()
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
            EditPriceRuleForm(
                priceRule = priceRule,
                onSaveClick = { updated ->
                    val id = updated.id ?: return@EditPriceRuleForm
                    viewModel.updatePriceRule(id, updated)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (updateState) {
                is Response.Loading -> {
//                LoadingIndicator()
                }
                is Response.Success -> {
                    LaunchedEffect(Unit) {
                        navController.popBackStack()
                    }
                }
                is Response.Failure -> {
                    Text(
                        text = "Error: ${(updateState as Response.Failure).error.message}",
                        color = Color.Red
                    )
                }
            }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditPriceRuleForm(
    priceRule: PriceRulesItem,
    onSaveClick: (PriceRulesItem) -> Unit
) {
    Log.i("EditPriceRuleForm", "priceRule: $priceRule")
    var title by remember { mutableStateOf(priceRule.title ?: "") }
    var value by remember { mutableStateOf(priceRule.value ?: "") }
    var valueType by remember { mutableStateOf(priceRule.valueType ?: "fixed_amount") }
    var allocationMethod by remember { mutableStateOf(priceRule.allocationMethod ?: "each") }
    var usageLimit by remember { mutableStateOf(priceRule.usageLimit ?: "0") }
    var oncePerCustomer by remember { mutableStateOf(priceRule.oncePerCustomer ?: false) }

    var startsAt by remember { mutableStateOf(priceRule.startsAt?.toLocalDate() ?: LocalDate.now()) }
    var endsAt by remember { mutableStateOf(priceRule.endsAt?.toLocalDate() ?: startsAt.plusDays(7)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Image
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

        // Value Type (fixed_amount or percentage)
        DropdownField(
            label = "Value Type",
            selected = valueType,
            options = listOf("fixed_amount", "percentage")
        ) { valueType = it }

//        // Allocation Method
//        DropdownField(
//            label = "Allocation",
//            selected = allocationMethod,
//            options = listOf("each", "across")
//        ) { allocationMethod = it }

        OutlinedTextField(
            usageLimit,
            { usageLimit = it },
            label = { Text("Usage Limit") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        // Once Per Customer
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Use code once per Customer")
            Spacer(modifier = Modifier.weight(1f))
            Switch(checked = oncePerCustomer, onCheckedChange = { oncePerCustomer = it })
        }

        // Start and End Date
        DatePickerField("Starts At", startsAt) { startsAt = it }
        DatePickerField("Ends At", endsAt) { endsAt = it }

        Button(
            onClick = {
                val updated = priceRule.copy(
                    title = title,
                    value = value,
                    valueType = valueType,
                    allocationMethod = "across",
                    usageLimit = usageLimit,
                    oncePerCustomer = oncePerCustomer,
                    startsAt = startsAt.toISOString(),
                    endsAt = endsAt.toISOString()
                )
                onSaveClick(updated)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Price Rule Changes")
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerField(label: String, date: LocalDate, onDateSelected: (LocalDate) -> Unit) {
    val context = LocalContext.current
    val formatter = DateTimeFormatter.ofPattern("d MMM yyyy")

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        OutlinedTextField(
            value = date.format(formatter),
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = {
            val picker = DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
                },
                date.year,
                date.monthValue - 1,
                date.dayOfMonth
            )
            picker.datePicker.minDate = System.currentTimeMillis()
            picker.show()
        }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_date_picker),
                contentDescription = "Pick Date"
            )
        }
    }
}




@RequiresApi(Build.VERSION_CODES.O)
fun String.toLocalDate(): LocalDate =
    try {
        OffsetDateTime.parse(this).toLocalDate()
    } catch (e: Exception) {
        LocalDate.now()
    }

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toISOString(): String =
    this.atTime(LocalTime.MAX).atOffset(ZoneOffset.UTC).toString()











