package eg.gov.iti.yallabuyadmin.inventory

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import eg.gov.iti.yallabuyadmin.model.InventoryItemUiModel
import eg.gov.iti.yallabuyadmin.model.Response
import eg.gov.iti.yallabuyadmin.utils.Constants.SHOPIFY_LOCATION_ID
import eg.iti.mad.climaguard.repo.Repository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class InventoryViewModel(private val repo: Repository) : ViewModel() {

    private val _inventoryItems = MutableStateFlow<Response<List<InventoryItemUiModel>>>(Response.Loading)
    val inventoryItems = _inventoryItems.asStateFlow()

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _filteredInventoryItems = MutableStateFlow<List<InventoryItemUiModel>>(emptyList())
    val filteredInventoryItems = _filteredInventoryItems.asStateFlow()

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
        filterInventory()
    }

    fun fetchInventoryItems() {
        viewModelScope.launch {
            repo.getInventoryItems()
                .catch { _inventoryItems.value = Response.Failure(it) }
                .collect {
                    _inventoryItems.value = Response.Success(it)
                    filterInventory()
                }
        }
    }

    fun updateVariantQuantity(inventoryItemId: Long, newQuantity: Int) {
        viewModelScope.launch {
            repo.setInventory(
                inventoryItemId = inventoryItemId,
                locationId = SHOPIFY_LOCATION_ID,
                available = newQuantity
            )
                .catch { e ->
                    _toastMessage.emit("Update failed: ${e.message}")
                }
                .collect {
                    _toastMessage.emit("Quantity updated to $newQuantity")
                    updateLocalQuantity(inventoryItemId, newQuantity)
                }
        }
    }

    fun updateLocalQuantity(inventoryItemId: Long, newQuantity: Int) {
        val current = (_inventoryItems.value as? Response.Success)?.data?.toMutableList() ?: return
        val updated = current.map {
            if (it.inventoryItemId == inventoryItemId) it.copy(quantity = newQuantity)
            else it
        }
        _inventoryItems.value = Response.Success(updated)
        filterInventory()
    }

    private fun filterInventory() {
        val query = _searchQuery.value.lowercase().trim()
        val items = (_inventoryItems.value as? Response.Success)?.data ?: emptyList()
        _filteredInventoryItems.value = if (query.isBlank()) {
            items
        } else {
            items.filter {
                it.title.lowercase().contains(query) || it.variantTitle.lowercase().contains(query)
            }
        }
    }
}


