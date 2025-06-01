package eg.gov.iti.yallabuyadmin.network

import eg.gov.iti.yallabuyadmin.model.ProductsResponse
import eg.gov.iti.yallabuyadmin.network.api.ShopifyApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class RemoteDataSourceImpl(private val services: ShopifyApi): RemoteDataSource {
    override suspend fun getAllProducts(): Flow<ProductsResponse> {
        val response = services.getProducts()
        return flowOf(response)
    }
}