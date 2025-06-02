package eg.iti.mad.climaguard.repo

import eg.gov.iti.yallabuyadmin.model.ProductsItem
import eg.gov.iti.yallabuyadmin.model.ProductsResponse
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun getAllProducts(): Flow<ProductsResponse>
    suspend fun deleteProduct(id: Long): Flow<Boolean>
    suspend fun getProductById(id: Long): Flow<ProductsItem>


}