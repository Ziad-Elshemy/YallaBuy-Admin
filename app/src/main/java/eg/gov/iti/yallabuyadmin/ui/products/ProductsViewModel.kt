package eg.gov.iti.yallabuyadmin.ui.products

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import eg.gov.iti.yallabuyadmin.domain.model.Product
import eg.gov.iti.yallabuyadmin.ui.utils.Response
import eg.gov.iti.yallabuyadmin.domain.repository.Repository
import eg.gov.iti.yallabuyadmin.domain.usecase.GetAllProductsToAdmin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class ProductsViewModel(private val repo: Repository) : ViewModel() {
    private val TAG = "ProductsViewModel"

    private val _products = mutableStateOf<List<Product?>?>(emptyList())
    val products: State<List<Product?>?> = _products

    private val _allProducts = MutableStateFlow<Response<List<Product?>?>>(Response.Loading)
    val allProducts = _allProducts.asStateFlow()

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()



    fun fetchProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = GetAllProductsToAdmin(repo).invoke()
//                val response = repo.getAllProducts()
                response
                    .catch { ex ->
                        _allProducts.value = Response.Failure(ex)
                        _toastMessage.emit("Error From Api ${ex.message}")
                        Log.e("ProductViewModel", "Error collecting products")
                    }
                    .collect{ data ->

                        _allProducts.value = Response.Success(data)

                    }
            } catch (ex: Exception) {
                _toastMessage.emit("Error from coroutines ${ex.message}")
                _allProducts.value = Response.Failure(ex)
                Log.e("ProductViewModel", "Error fetching products: ${ex.message}")
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
                            fetchProducts()
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

class ProductsFactory(private val repo: Repository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProductsViewModel(repo) as T
    }

}
