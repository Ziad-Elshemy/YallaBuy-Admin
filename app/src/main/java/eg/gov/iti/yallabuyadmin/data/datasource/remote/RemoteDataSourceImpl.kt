package eg.gov.iti.yallabuyadmin.data.datasource.remote

import eg.gov.iti.yallabuyadmin.data.dto.ProductsItem
import eg.gov.iti.yallabuyadmin.data.dto.ProductsResponseDto
import eg.gov.iti.yallabuyadmin.data.repository.RemoteDataSource
import eg.gov.iti.yallabuyadmin.data.datasource.remote.api.ShopifyApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class RemoteDataSourceImpl(private val services: ShopifyApi): RemoteDataSource {
    override suspend fun getAllProducts(): Flow<ProductsResponseDto> {
        val response = services.getProducts()
        return flowOf(response)
    }

    override suspend fun deleteProduct(id: Long): Flow<Boolean> {
        val response = services.deleteProduct(id)
        return flowOf(response.isSuccessful)
    }

    override suspend fun getProductById(id: Long): Flow<ProductsItem> {
        val response = services.getProductById(id)
        return flowOf(response)
    }
}