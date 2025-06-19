package eg.gov.iti.yallabuyadmin.addproduct

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import eg.gov.iti.yallabuyadmin.model.CreateProductRequest
import eg.gov.iti.yallabuyadmin.model.FixedCollection
import eg.gov.iti.yallabuyadmin.model.ProductsItem
import eg.gov.iti.yallabuyadmin.model.Response
import eg.iti.mad.climaguard.repo.Repository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch


class CreateProductViewModel(private val repo: Repository) : ViewModel() {

    private val _createState = MutableStateFlow<Response<ProductsItem>>(Response.Loading)
    val createState = _createState.asStateFlow()

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    private val _vendors = MutableStateFlow<Response<List<String>>>(Response.Loading)
    val vendors = _vendors.asStateFlow()

    private val _productTypes = MutableStateFlow<Response<List<String>>>(Response.Loading)
    val productTypes = _productTypes.asStateFlow()

    fun loadInitialData() {
        getAllVendors()
        getAllProductTypes()
    }

    private fun getAllVendors() {
        viewModelScope.launch {
            try {
                repo.getAllVendors()
                    .map { response ->
                        response?.products
                            ?.mapNotNull { it?.vendor }
                            ?.distinct()
                            ?: emptyList()
                    }
                    .collect { vendorsList ->
                        _vendors.value = Response.Success(vendorsList)
                    }
            } catch (e: Exception) {
                _vendors.value = Response.Failure(e)
            }
        }
    }


    private fun getAllProductTypes() {
        viewModelScope.launch {
            try {
                repo.getAllProductTypes()
                    .map { response ->
                        response?.products
                            ?.mapNotNull { it?.productType }
                            ?.distinct()
                            ?: emptyList()
                    }
                    .collect { productTypesList ->
                        _productTypes.value = Response.Success(productTypesList)
                    }
            } catch (e: Exception) {
                _productTypes.value = Response.Failure(e)
            }
        }
    }


    fun createProduct(product: ProductsItem, selectedCollectionId: Long) {
        viewModelScope.launch {
            _createState.value = Response.Loading

            repo.createProduct(product)
                .catch { ex ->
                    _createState.value = Response.Failure(ex)
                    _toastMessage.emit("Error: ${ex.message}")
                }
                .collect { createdProduct ->
                    if (createdProduct != null){
                        _createState.value = Response.Success(createdProduct)
                        _toastMessage.emit("Product created successfully")
                        createdProduct.id?.let { productId ->
                            repo.assignProductToCollection(productId, selectedCollectionId)
                                .catch { ex ->
                                    _toastMessage.emit("Collection Assignment Error: ${ex.message}")
                                    Log.i("createProduct Collection", "Error: ${ex.message} ")
                                }
                                .collect{
                                    _toastMessage.emit("added to collection successfully")
                                    Log.i("createProduct Collection", "added to collection successfully")
                                }
                        }
                    }
                    else{
                        _toastMessage.emit("Failed to create Product")
                    }

                }
        }
    }
}

