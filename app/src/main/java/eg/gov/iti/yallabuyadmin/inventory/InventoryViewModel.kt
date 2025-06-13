package eg.gov.iti.yallabuyadmin.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import eg.gov.iti.yallabuyadmin.model.InventoryItemUiModel
import eg.gov.iti.yallabuyadmin.model.Response
import eg.iti.mad.climaguard.repo.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class InventoryViewModel(private val repo: Repository) : ViewModel() {
    private val TAG = "InventoryViewModel"

    private val _inventoryItems = MutableStateFlow<Response<List<InventoryItemUiModel>>>(Response.Loading)
    val inventoryItems = _inventoryItems.asStateFlow()

    fun fetchInventoryItems() {
        viewModelScope.launch {
            repo.getInventoryItems()
                .catch { _inventoryItems.value = Response.Failure(it) }
                .collect { _inventoryItems.value = Response.Success(it) }
        }
    }

}
