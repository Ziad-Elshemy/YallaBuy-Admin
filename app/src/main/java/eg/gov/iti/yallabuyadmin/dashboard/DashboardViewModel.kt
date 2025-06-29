package eg.gov.iti.yallabuyadmin.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.gov.iti.yallabuyadmin.model.DashboardData
import eg.gov.iti.yallabuyadmin.model.Response
import eg.iti.mad.climaguard.repo.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardViewModel(private val repo: Repository) : ViewModel() {

    private val _dashboardData = MutableStateFlow<Response<DashboardData>>(Response.Loading)
    val dashboardData = _dashboardData.asStateFlow()

    fun fetchDashboardData() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    combine(
                        repo.getAllProducts(),
                        repo.getInventoryItems(),
                        repo.getAllPriceRules(),
                        repo.getAllDiscountCodes(),
                        repo.getAllVendors()
                    ) { productsResp, inventoryItems, priceRules, discounts, vendorsResp ->

                        val totalVendors = vendorsResp?.products
                            ?.mapNotNull { it?.vendor }
                            ?.distinct()
                            ?.size ?: 0

                        val dashboard = DashboardData(
                            productCount = productsResp?.products?.size ?: 0,
                            priceRuleCount = priceRules.size,
                            discountCount = discounts.size,
                            vendorsCount = totalVendors,
                            inventoryItemsCount = inventoryItems.size
                        )

                        Response.Success(dashboard)
                    }
                        .catch { e -> _dashboardData.emit(Response.Failure(e)) }
                        .collect { result -> _dashboardData.emit(result) }
                }
            } catch (e: Exception) {
                _dashboardData.emit(Response.Failure(e))
            }
        }

    }

}
