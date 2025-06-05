package eg.gov.iti.yallabuyadmin.network

import eg.gov.iti.yallabuyadmin.model.AddImageRequest
import eg.gov.iti.yallabuyadmin.model.ImagesItem
import eg.gov.iti.yallabuyadmin.model.ProductsItem
import eg.gov.iti.yallabuyadmin.model.ProductsResponse
import eg.gov.iti.yallabuyadmin.model.UpdateProductRequest
import eg.gov.iti.yallabuyadmin.network.api.ShopifyApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class RemoteDataSourceImpl(private val services: ShopifyApi): RemoteDataSource {
    override suspend fun getAllProducts(): Flow<ProductsResponse> {
        val response = services.getProducts()
        return flowOf(response)
    }

    override suspend fun deleteProduct(id: Long): Flow<Boolean> {
        val response = services.deleteProduct(id)
        return flowOf(response.isSuccessful)
    }

    override suspend fun getProductById(id: Long): Flow<ProductsItem?> {
        val response = services.getProductById(id)
        return flowOf(response.product)
    }

    override suspend fun updateProduct(
        id: Long,
        productBody: UpdateProductRequest
    ): Flow<ProductsItem?> {
        val response = services.updateProduct(id,productBody)
        if (response.isSuccessful) {
            return flowOf(response.body())
        } else {
            throw Exception("Update failed with code ${response.code()}")
        }
    }

    override suspend fun addProductImage(id: Long, imageBody: AddImageRequest): Flow<ImagesItem?> {
        val response = services.addProductImage(id,imageBody)
        if (response.isSuccessful){
            return flowOf(response.body())
        }
        else{
            throw Exception("Add image failed with code ${response.code()}")
        }
    }

    override suspend fun deleteProductImage(productId: Long, imageId: Long): Flow<Unit?> {
        val response = services.deleteProductImage(productId,imageId)
        if (response.isSuccessful){
            return flowOf(response.body())
        } else {
            throw Exception("Delete image failed with code ${response.code()}")
        }
    }
}