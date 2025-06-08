package eg.iti.mad.climaguard.repo

import eg.gov.iti.yallabuyadmin.database.LocalDataSource
import eg.gov.iti.yallabuyadmin.model.AddImageRequest
import eg.gov.iti.yallabuyadmin.model.ImagesItem
import eg.gov.iti.yallabuyadmin.model.PriceRulesItem
import eg.gov.iti.yallabuyadmin.model.ProductsItem
import eg.gov.iti.yallabuyadmin.model.ProductsResponse
import eg.gov.iti.yallabuyadmin.model.UpdateProductRequest
import eg.gov.iti.yallabuyadmin.network.RemoteDataSource
import kotlinx.coroutines.flow.Flow

class RepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
): Repository {

    private val TAG = "RepositoryImpl"


    override suspend fun getAllProducts(): Flow<ProductsResponse> {
        return remoteDataSource.getAllProducts()
    }

    override suspend fun deleteProduct(id: Long): Flow<Boolean> {
        return remoteDataSource.deleteProduct(id)
    }

    override suspend fun getProductById(id: Long): Flow<ProductsItem?> {
        return remoteDataSource.getProductById(id)
    }

    override suspend fun updateProduct(
        id: Long,
        productBody: UpdateProductRequest
    ): Flow<ProductsItem?> {
        return remoteDataSource.updateProduct(id,productBody)
    }

    override suspend fun addProductImage(id: Long, imageBody: AddImageRequest): Flow<ImagesItem?> {
        return remoteDataSource.addProductImage(id,imageBody)
    }

    override suspend fun deleteProductImage(productId: Long, imageId: Long): Flow<Unit?> {
        return remoteDataSource.deleteProductImage(productId,imageId)
    }

    override suspend fun createProduct(product: ProductsItem): Flow<ProductsItem> {
        return remoteDataSource.createProduct(product)
    }

    override suspend fun getAllVendors(): Flow<ProductsResponse> {
        return remoteDataSource.getAllVendors()
    }

    override suspend fun getAllProductTypes(): Flow<ProductsResponse> {
        return remoteDataSource.getAllProductTypes()
    }

    override suspend fun setInventory(locationId: Long, inventoryItemId: Long, available: Int): Flow<Int> {
        return remoteDataSource.setInventory(locationId, inventoryItemId, available)
    }

    override suspend fun getAllPriceRules(): Flow<List<PriceRulesItem>> {
        return remoteDataSource.getAllPriceRules()
    }

    companion object {
        private var INSTANCE: RepositoryImpl? = null
        fun getInstance(localDataSource: LocalDataSource, remoteDataSource: RemoteDataSource): RepositoryImpl{
            return INSTANCE ?: synchronized(this){
                val instance = RepositoryImpl(localDataSource, remoteDataSource)
                INSTANCE = instance
                instance
            }
        }
    }

}