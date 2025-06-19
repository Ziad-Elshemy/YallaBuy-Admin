package eg.gov.iti.yallabuyadmin.products

import android.util.Log
import androidx.compose.runtime.State
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

    private val _products = mutableStateOf<List<ProductsItem?>?>(emptyList())
    val products: State<List<ProductsItem?>?> = _products

    private val _allProducts = MutableStateFlow<Response<List<ProductsItem?>?>>(Response.Loading)
    val allProducts = _allProducts.asStateFlow()

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()



    fun fetchProductsItems() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.getAllProducts()
                    .catch { ex ->
                        _allProducts.value = Response.Failure(ex)
                        _toastMessage.emit("Error From Api ${ex.message}")
                        Log.e("ProductsItemViewModel", "Error collecting products")
                    }
                    .collect{ data ->

                        _allProducts.value = Response.Success(data?.products)

                    }
            } catch (ex: Exception) {
                _toastMessage.emit("Error from coroutines ${ex.message}")
                _allProducts.value = Response.Failure(ex)
                Log.e("ProductsItemViewModel", "Error fetching products: ${ex.message}")
            }
        }
    }

    fun deleteProductById(id: Long) {
        viewModelScope.launch {
            try {
                val response = repo.deleteProduct(id)
                response
                    .catch {
                        _toastMessage.emit("Failed to delete product: ${id}")
                        Log.e("Delete", "Failed to delete product: ${id}")
                    }
                    .collect{isSuccessful ->
                        if (isSuccessful){
                            _toastMessage.emit("Product $id deleted successfully")
                            fetchProductsItems()
                            Log.d("Delete", "Product $id deleted successfully")
                        }else{
                            _toastMessage.emit("Failed to delete product: ${id}")
                            Log.e("Delete", "Failed to delete product: ${id}")
                        }
                    }
            } catch (e: Exception) {
                Log.e("Delete", "Exception: ${e.message}")
            }
        }
    }



}
