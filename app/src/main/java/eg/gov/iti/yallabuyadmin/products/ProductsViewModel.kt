package eg.gov.iti.yallabuyadmin.products

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import eg.gov.iti.yallabuyadmin.model.ProductsItem
import eg.gov.iti.yallabuyadmin.model.Response
import eg.iti.mad.climaguard.repo.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class ProductsViewModel(private val repo: Repository) : ViewModel() {
    private val TAG = "ProductsItemsViewModel"

    private val _allProducts = MutableStateFlow<Response<List<ProductsItem?>?>>(Response.Loading)
    val allProducts = _allProducts.asStateFlow()

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _filteredProducts = MutableStateFlow<List<ProductsItem?>?>(emptyList())
    val filteredProducts = _filteredProducts.asStateFlow()

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
        filterProducts()
    }

    fun fetchProductsItems() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.getAllProducts()
                    .catch { ex ->
                        _allProducts.value = Response.Failure(ex)
                        _toastMessage.emit("Error From Api ${ex.message}")
                    }
                    .collect { data ->
                        val list = data?.products
                        _allProducts.value = Response.Success(list)
                        filterProducts()
                    }
            } catch (ex: Exception) {
                _toastMessage.emit("Error from coroutines ${ex.message}")
                _allProducts.value = Response.Failure(ex)
            }
        }
    }

    fun deleteProductById(id: Long) {
        viewModelScope.launch {
            try {
                repo.deleteProduct(id)
                    .catch {
                        _toastMessage.emit("Failed to delete product: ${id}")
                    }
                    .collect { isSuccessful ->
                        if (isSuccessful) {
                            _toastMessage.emit("Product $id deleted successfully")
                            fetchProductsItems()
                        } else {
                            _toastMessage.emit("Failed to delete product: ${id}")
                        }
                    }
            } catch (e: Exception) {
                _toastMessage.emit("Unexpected Error: ${e.message}")
            }
        }
    }

    private fun filterProducts() {
        val query = _searchQuery.value.lowercase().trim()
        val list = (_allProducts.value as? Response.Success)?.data ?: emptyList()
        _filteredProducts.value = if (query.isBlank()) {
            list
        } else {
            list.filter { it?.title?.lowercase()?.contains(query) == true }
        }
    }
}


