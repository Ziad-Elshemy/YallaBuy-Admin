package eg.gov.iti.yallabuyadmin.addproduct

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import eg.gov.iti.yallabuyadmin.model.CreateProductRequest
import eg.gov.iti.yallabuyadmin.model.ProductsItem
import eg.gov.iti.yallabuyadmin.model.Response
import eg.iti.mad.climaguard.repo.Repository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class CreateProductViewModel(private val repo: Repository) : ViewModel() {

    private val _createState = MutableStateFlow<Response<ProductsItem>>(Response.Loading)
    val createState = _createState.asStateFlow()

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    fun createProduct(product: ProductsItem) {
        viewModelScope.launch {
            _createState.value = Response.Loading

            repo.createProduct(product)
                .catch { ex ->
                    _createState.value = Response.Failure(ex)
                    _toastMessage.emit("Error: ${ex.message}")
                }
                .collect { result ->
                    _createState.value = Response.Success(result)
                    _toastMessage.emit("Product created successfully 🎉")
                }
        }
    }
}

class CreateProductFactory(private val repo: Repository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CreateProductViewModel(repo) as T
    }

}
