package eg.iti.mad.climaguard.repo

import eg.gov.iti.yallabuyadmin.database.LocalDataSource
import eg.gov.iti.yallabuyadmin.model.AddImageRequest
import eg.gov.iti.yallabuyadmin.model.CollectItem
import eg.gov.iti.yallabuyadmin.model.DiscountCode
import eg.gov.iti.yallabuyadmin.model.ImagesItem
import eg.gov.iti.yallabuyadmin.model.InventoryItemUiModel
import eg.gov.iti.yallabuyadmin.model.PriceRulesItem
import eg.gov.iti.yallabuyadmin.model.ProductsItem
import eg.gov.iti.yallabuyadmin.model.ProductsResponse
import eg.gov.iti.yallabuyadmin.model.UpdateProductRequest
import eg.gov.iti.yallabuyadmin.network.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
): Repository {

    private val TAG = "RepositoryImpl"


    override suspend fun getAllProducts(): Flow<ProductsResponse?> {
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

    override suspend fun createProduct(product: ProductsItem): Flow<ProductsItem?> {
        return remoteDataSource.createProduct(product)
    }

    override suspend fun getAllVendors(): Flow<ProductsResponse?> {
        return remoteDataSource.getAllVendors()
    }

    override suspend fun getAllProductTypes(): Flow<ProductsResponse?> {
        return remoteDataSource.getAllProductTypes()
    }

    override suspend fun setInventory(locationId: Long, inventoryItemId: Long, available: Int): Flow<Int> {
        return remoteDataSource.setInventory(locationId, inventoryItemId, available)
    }

    override suspend fun getAllPriceRules(): Flow<List<PriceRulesItem>> {
        return remoteDataSource.getAllPriceRules()
    }

    override suspend fun updatePriceRule(id: Long, rule: PriceRulesItem): Flow<PriceRulesItem> {
        return remoteDataSource.updatePriceRule(id, rule)
    }

    override suspend fun createPriceRule(rule: PriceRulesItem): Flow<PriceRulesItem> {
        return remoteDataSource.createPriceRule(rule)
    }

    override suspend fun deletePriceRule(priceRuleId: Long): Flow<Boolean>  = flow {
        val result = remoteDataSource.deletePriceRule(priceRuleId)
        emit(result)
    }

    override suspend fun getAllDiscountCodes(): Flow<List<DiscountCode>>  = flow {
        val rules = getAllPriceRulesSync()
        val allDiscounts = rules
            .filter { it.id != null }
            .flatMap { rule ->
                remoteDataSource.getDiscountCodesByPriceRuleId(rule.id!!)
            }
        emit(allDiscounts)
    }

    override suspend fun getAllPriceRulesSync(): List<PriceRulesItem> {
        val response = remoteDataSource.getAllPriceRulesRaw()
        if (response.isSuccessful) {
            return response.body()?.priceRules?.filterNotNull() ?: emptyList()
        } else {
            throw Exception("Failed to fetch price rules: ${response.code()}")
        }
    }


    override suspend fun deleteDiscountCode(
        priceRuleId: Long,
        discountCodeId: Long
    ): Flow<Boolean>  = flow {
        val result = remoteDataSource.deleteDiscountCode(priceRuleId, discountCodeId)
        emit(result)
    }

    override suspend fun updateDiscountCode(
        priceRuleId: Long,
        discountCodeId: Long,
        discountCode: DiscountCode
    ): Flow<DiscountCode> {
        return remoteDataSource.updateDiscountCode(priceRuleId, discountCodeId, discountCode)
    }

    override suspend fun createDiscountCode(
        ruleId: Long,
        discountCode: DiscountCode
    ): Flow<DiscountCode> {
        return remoteDataSource.createDiscountCode(ruleId,discountCode)
    }


    override suspend fun getInventoryItems(): Flow<List<InventoryItemUiModel>> = flow {
        val products = remoteDataSource.getAllProductsWithVariants()

        val inventoryItems = products.flatMap { product ->
            product.variants?.mapNotNull { variant ->
                InventoryItemUiModel(
                    title = product.title ?: "",
                    imageUrl = product.image?.src,
                    variantTitle = variant?.title ?: "",
                    inventoryItemId = variant?.inventoryItemId ?: 0L,
                    quantity = variant?.inventoryQuantity ?: 0,
                    price = variant?.price ?: "0.0"
                )
            } ?: emptyList()
        }

        emit(inventoryItems)
    }


    override suspend fun assignProductToCollection(
        productId: Long,
        collectionId: Long
    ): Flow<Unit> {
        return remoteDataSource.assignProductToCollection(productId,collectionId)
    }

    override suspend fun deleteProductFromAllCollections(productId: Long): Flow<Unit> = flow {
        val collects = remoteDataSource.getCollectsForProduct(productId)
        collects.forEach {
            remoteDataSource.deleteCollect(it.id)
        }
        emit(Unit)
    }

    override suspend fun getCollectsForProduct(productId: Long): Flow<Long> = flow {
        if (remoteDataSource.getCollectsForProduct(productId).isEmpty()){
            emit(0L)
        }else{
            val result = remoteDataSource.getCollectsForProduct(productId).get(0).collection_id
            emit(result)
        }

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