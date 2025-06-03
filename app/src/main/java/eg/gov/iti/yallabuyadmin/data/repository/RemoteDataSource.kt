package eg.gov.iti.yallabuyadmin.data.repository

import eg.gov.iti.yallabuyadmin.data.dto.ProductsItem
import eg.gov.iti.yallabuyadmin.data.dto.ProductsResponseDto
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {

    suspend fun getAllProducts(): Flow<ProductsResponseDto>
    suspend fun deleteProduct(id: Long): Flow<Boolean>
    suspend fun getProductById(id: Long): Flow<ProductsItem>

}