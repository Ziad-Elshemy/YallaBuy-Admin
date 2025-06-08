package eg.gov.iti.yallabuyadmin.coupons


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import eg.gov.iti.yallabuyadmin.R
import eg.gov.iti.yallabuyadmin.model.PriceRulesItem
import eg.gov.iti.yallabuyadmin.model.Response
import eg.gov.iti.yallabuyadmin.products.LoadingIndicator


@Composable
fun CouponsScreen(navController: NavController, viewModel: CouponsViewModel) {
    val uiState by viewModel.priceRules.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF2CABAB),
            Color(0xFFE1F5FE)
        )
    )

    LaunchedEffect(Unit) {
        viewModel.fetchPriceRules()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = backgroundBrush)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            when (uiState) {
                is Response.Loading -> {
                    LoadingIndicator()
                }

                is Response.Success -> {
                    val rules = (uiState as Response.Success<List<PriceRulesItem>>).data
                    PriceRulesScreen(
                        rules = rules,
                        onEditClick = {
//                            navController.navigate("editPriceRule/${it.id}")
                        }
                    )
                }

                is Response.Failure -> {
                    Text(
                        text = "Failed to load Price Rules",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}



@Composable
fun PriceRulesScreen(
    rules: List<PriceRulesItem>,
    onEditClick: (PriceRulesItem) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Price Rules", style = MaterialTheme.typography.titleLarge)
            Row {
                IconButton(onClick = { /* Search Click */ }) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
                IconButton(onClick = { /* Add New Rule */ }) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        LazyColumn {
            items(rules) { rule ->
                PriceRuleItemCard(rule, onEditClick)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun PriceRuleItemCard(
    rule: PriceRulesItem,
    onEditClick: (PriceRulesItem) -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
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

            IconButton(onClick = { onEditClick(rule) }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
        }
    }
}











