package eg.gov.iti.yallabuyadmin.productdetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import eg.gov.iti.yallabuyadmin.model.ProductsItem
import eg.gov.iti.yallabuyadmin.model.Response
import eg.gov.iti.yallabuyadmin.model.UpdateProductRequest
import eg.iti.mad.climaguard.repo.Repository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class ProductDetailsViewModel(private val repo: Repository) : ViewModel() {
    private val TAG = "ProductDetailsViewModel"


    private val _productDetails = MutableStateFlow<Response<ProductsItem?>>(Response.Loading)
    val productDetails = _productDetails.asStateFlow()

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    fun fetchProductById(id: Long) {
        viewModelScope.launch {
            val response = repo.getProductById(id)
            Log.e(TAG, "fetchProductById: called with id = $id", )
            response
                .catch { ex ->
                    _productDetails.value = Response.Failure(ex)
                }
                .collect {
                    _productDetails.value = Response.Success(it)
                    Log.e(TAG, "fetchProductById: called with title = ${it?.title} ", )
                }
        }
    }

    fun updateProductDetails(productId: Long, body: UpdateProductRequest) {
        viewModelScope.launch {
            repo.updateProduct(productId, body)
                .catch { exception ->
                    Log.e("UpdateProduct", "Error updating product: ${exception.message}")
                    _toastMessage.emit("Update failed: ${exception.message}")
                }
                .collect { updatedProduct ->
                    if (updatedProduct != null) {
                        Log.d("UpdateProduct", "Product updated: ${updatedProduct.title}")
                        _toastMessage.emit("Product updated successfully")
                        fetchProductById(productId)
                    } else {
                        _toastMessage.emit("Update failed: empty response")
                    }
                }
        }
    }


}

