package eg.gov.iti.yallabuyadmin.network

import eg.gov.iti.yallabuyadmin.model.ProductsResponse
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {

    suspend fun getAllProducts(): Flow<ProductsResponse>

}