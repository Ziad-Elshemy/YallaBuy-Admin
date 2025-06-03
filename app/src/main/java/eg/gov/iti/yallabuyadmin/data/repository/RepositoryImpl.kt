package eg.gov.iti.yallabuyadmin.data.repository

import eg.gov.iti.yallabuyadmin.data.dto.ProductsItem
import eg.gov.iti.yallabuyadmin.data.dto.ProductsResponseDto
import eg.gov.iti.yallabuyadmin.data.repository.mappers.toProduct
import eg.gov.iti.yallabuyadmin.domain.model.Product
import eg.gov.iti.yallabuyadmin.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
): Repository {

    private val TAG = "RepositoryImpl"


    override suspend fun getAllProducts(): Flow<List<Product>> {
        return remoteDataSource.getAllProducts()
            .map { dto -> dto.products?.filterNotNull()?.map { it.toProduct() } ?: emptyList() }
    }


    override suspend fun deleteProduct(id: Long): Flow<Boolean> {
        return remoteDataSource.deleteProduct(id)
    }

    override suspend fun getProductById(id: Long): Flow<ProductsItem> {
        return remoteDataSource.getProductById(id)
    }


    companion object {
        private var INSTANCE: RepositoryImpl? = null
        fun getInstance(localDataSource: LocalDataSource, remoteDataSource: RemoteDataSource): RepositoryImpl {
            return INSTANCE ?: synchronized(this){
                val instance = RepositoryImpl(localDataSource, remoteDataSource)
                INSTANCE = instance
                instance
            }
        }
    }

}