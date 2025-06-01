package eg.iti.mad.climaguard.repo

import eg.gov.iti.yallabuyadmin.model.ProductsResponse
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun getAllProducts(): Flow<ProductsResponse>


}