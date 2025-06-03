package eg.gov.iti.yallabuyadmin.domain.repository

import eg.gov.iti.yallabuyadmin.data.dto.ProductsItem
import eg.gov.iti.yallabuyadmin.data.dto.ProductsResponseDto
import eg.gov.iti.yallabuyadmin.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun getAllProducts(): Flow<List<Product>>
    suspend fun deleteProduct(id: Long): Flow<Boolean>
    suspend fun getProductById(id: Long): Flow<ProductsItem>


}