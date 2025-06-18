package eg.gov.iti.yallabuyadmin.productdetails

import android.util.Log
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.gov.iti.yallabuyadmin.model.AddImageRequest
import eg.gov.iti.yallabuyadmin.model.FixedCollection
import eg.gov.iti.yallabuyadmin.model.ProductsItem
import eg.gov.iti.yallabuyadmin.model.Response
import eg.gov.iti.yallabuyadmin.model.UpdateProductRequest
import eg.gov.iti.yallabuyadmin.model.fixedCollections
import eg.gov.iti.yallabuyadmin.utils.Constants.SHOPIFY_LOCATION_ID
import eg.iti.mad.climaguard.repo.Repository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class ProductDetailsViewModel(private val repo: Repository) : ViewModel() {
    private val TAG = "ProductDetailsViewModel"


    private val _productDetails = MutableStateFlow<Response<ProductsItem?>>(Response.Loading)
    val productDetails = _productDetails.asStateFlow()

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    private val _vendors = MutableStateFlow<Response<List<String>>>(Response.Loading)
    val vendors = _vendors.asStateFlow()

    private val _productTypes = MutableStateFlow<Response<List<String>>>(Response.Loading)
    val productTypes = _productTypes.asStateFlow()

    private val _productCollectionId = MutableStateFlow<Response<FixedCollection?>>(Response.Loading)
    val productCollectionId = _productCollectionId.asStateFlow()


    fun loadInitialData(productId: Long) {
        getAllVendors()
        getAllProductTypes()
        getProductCollection(productId)
    }

    private fun getAllVendors() {
        viewModelScope.launch {
            repo.getAllVendors()
                .catch { _vendors.value = Response.Failure(it) }
                .map { response ->
                    response.products
                        ?.mapNotNull { it?.vendor }
                        ?.distinct()
                        ?: emptyList()
                }
                .collect { vendorsList ->
                    _vendors.value = Response.Success(vendorsList)
                }
        }
    }

    private fun getAllProductTypes() {
        viewModelScope.launch {
            repo.getAllProductTypes()
                .catch { _productTypes.value = Response.Failure(it) }
                .map { response ->
                    response.products
                        ?.mapNotNull { it?.productType }
                        ?.distinct()
                        ?: emptyList()
                }
                .collect { productTypesList ->
                    _productTypes.value = Response.Success(productTypesList)
                }
        }
    }

    private fun getProductCollection(productId: Long) {
        viewModelScope.launch {
            repo.getCollectsForProduct(productId)
                .catch { _productCollectionId.value = Response.Failure(it) }
                .collect { productCollectionId ->
                    val productCollection = fixedCollections.firstOrNull {
                        it.id == productCollectionId
                    }
                    _productCollectionId.value = Response.Success(productCollection)

                }
        }
    }

    fun fetchProductById(id: Long) {
        viewModelScope.launch {
            val response = repo.getProductById(id)
            Log.e(TAG, "fetchProductById: called with id = $id")
            response
                .catch { ex ->
                    _productDetails.value = Response.Failure(ex)
                }
                .collect {
                    _productDetails.value = Response.Success(it)
                    Log.e(TAG, "fetchProductById: called with title = ${it?.title} ")
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

                        body.product.variants?.forEach {
                            Log.d(TAG, "updateProductDetails: ${it?.inventoryQuantity}")
                        }


                        val updatedVariants = updatedProduct.variants ?: emptyList()
                        val requestedVariants = body.product.variants ?: emptyList()

                        updatedVariants.zip(requestedVariants)
                            .forEach { (shopifyVariant, inputVariant) ->
                                val inventoryItemId = shopifyVariant?.inventoryItemId
                                val quantity = inputVariant?.inventoryQuantity

                                if (inventoryItemId != null && quantity != null) {
                                    repo.setInventory(
                                        locationId = SHOPIFY_LOCATION_ID,
                                        inventoryItemId = inventoryItemId,
                                        available = quantity
                                    )
                                        .catch {
//                                        _toastMessage.emit("Update failed: ${it.message}")
                                            Log.e(
                                                TAG,
                                                "updateProductDetails: error = ${it.localizedMessage}",
                                            )
                                        }
                                        .collect { res ->
//                                        _toastMessage.emit("Inventory updated successfully q = $res")
                                            Log.d(TAG, "updateProductDetails: quantity = $res")
                                        }
                                }
                            }


                        fetchProductById(productId)
                    } else {
                        _toastMessage.emit("Update failed: empty response")
                    }
                }
        }
    }


    fun addImageToProduct(productId: Long, body: AddImageRequest) {
        viewModelScope.launch {
            repo.addProductImage(productId, body)
                .catch { ex ->
                    _toastMessage.emit("add image failed ${ex.message}")
                }
                .collect { imageItem ->
                    if (imageItem != null) {
                        _toastMessage.emit("Image added successfully")
                        fetchProductById(productId)
                    } else {
                        _toastMessage.emit("failed: empty response")
                    }
                }
        }
    }

    fun deleteProductImage(productId: Long, imageId: Long) {
        viewModelScope.launch {
            val response = repo.deleteProductImage(productId, imageId)
            response
                .catch { ex ->
                    _toastMessage.emit("delete image failed ${ex.message}")
                }
                .collect { unit ->
                    if (unit != null) {
                        _toastMessage.emit("Image deleted successfully")
                        fetchProductById(productId)
                    } else {
                        _toastMessage.emit("failed: empty response")
                    }
                }
        }
    }


    fun updateCollection(productId: Long, collectionId: Long) {

        viewModelScope.launch {
            val response = repo.deleteProductFromAllCollections(productId)
            response
                .catch { ex ->
                    _toastMessage.emit("delete collection failed ${ex.message}")
                }
                .collect {

                    _toastMessage.emit("collection deleted successfully")
                    repo.assignProductToCollection(productId, collectionId)
                        .catch { ex ->
                            _toastMessage.emit("Collection Assignment Error: ${ex.message}")
                            Log.i("assignProductToCollection", "Error: ${ex.message} ")
                        }
                        .collect {
                            _toastMessage.emit("collection updated successfully")
                            Log.i(
                                "assignProductToCollection",
                                "added to collection successfully"
                            )
                        }

                }
        }

    }


}

