package eg.gov.iti.yallabuyadmin.products

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import eg.gov.iti.yallabuyadmin.model.Product
import eg.iti.mad.climaguard.repo.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class ProductsViewModel(private val repo: Repository) : ViewModel() {
    private val TAG = "ProductsViewModel"

    private val _products = mutableStateOf<List<Product>>(emptyList())
    val products: State<List<Product>> = _products

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repo.getAllProducts()
                response
                    .catch {
                        Log.e("ProductViewModel", "Error collecting products")
                    }
                    .collect{ data ->

                        _products.value = data.products

                    }
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Error fetching products: ${e.message}")
            }
        }
    }


}

class ProductsFactory(private val repo: Repository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProductsViewModel(repo) as T
    }

}
