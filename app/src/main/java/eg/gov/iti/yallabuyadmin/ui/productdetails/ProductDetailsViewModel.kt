package eg.gov.iti.yallabuyadmin.ui.productdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.gov.iti.yallabuyadmin.data.dto.ProductsItem
import eg.gov.iti.yallabuyadmin.ui.utils.Response
import eg.gov.iti.yallabuyadmin.domain.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class ProductDetailsViewModel(private val repo: Repository) : ViewModel() {
    private val TAG = "ProductDetailsViewModel"


    private val _productDetails = MutableStateFlow<Response<ProductsItem?>>(Response.Loading)
    val productDetails = _productDetails.asStateFlow()

    fun fetchProductById(id: Long) {
        viewModelScope.launch {
            val response = repo.getProductById(id)

            response
                .catch { ex ->
                    _productDetails.value = Response.Failure(ex)
                }
                .collect {
                    _productDetails.value = Response.Success(it)
                }
        }
    }

}

