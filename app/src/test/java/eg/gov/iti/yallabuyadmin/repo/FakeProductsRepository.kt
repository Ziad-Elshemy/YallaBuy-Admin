package eg.gov.iti.yallabuyadmin.repo

import eg.gov.iti.yallabuyadmin.model.AddImageRequest
import eg.gov.iti.yallabuyadmin.model.DiscountCode
import eg.gov.iti.yallabuyadmin.model.ImagesItem
import eg.gov.iti.yallabuyadmin.model.InventoryItemUiModel
import eg.gov.iti.yallabuyadmin.model.PriceRulesItem
import eg.gov.iti.yallabuyadmin.model.ProductsItem
import eg.gov.iti.yallabuyadmin.model.ProductsResponse
import eg.gov.iti.yallabuyadmin.model.UpdateProductRequest
import eg.iti.mad.climaguard.repo.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeProductsRepository: Repository {

    var fakeProducts = listOf(
        ProductsItem(id = 1L, title = "Product 1"),
        ProductsItem(id = 2L, title = "Product 2")
    )

    override suspend fun getAllProducts(): Flow<ProductsResponse?> {
        return flowOf(ProductsResponse(products = fakeProducts.map { it }))
    }

    override suspend fun deleteProduct(id: Long): Flow<Boolean> {
        val isDeleted = fakeProducts.any { it.id == id }
        if (isDeleted) {
            fakeProducts = fakeProducts.filterNot { it.id == id }
        }
        return flowOf(isDeleted)
    }

    override suspend fun getProductById(id: Long): Flow<ProductsItem?> {
        TODO("Not yet implemented")
    }

    override suspend fun updateProduct(
        id: Long,
        productBody: UpdateProductRequest
    ): Flow<ProductsItem?> {
        TODO("Not yet implemented")
    }

    override suspend fun addProductImage(id: Long, imageBody: AddImageRequest): Flow<ImagesItem?> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProductImage(productId: Long, imageId: Long): Flow<Unit?> {
        TODO("Not yet implemented")
    }

    override suspend fun createProduct(product: ProductsItem): Flow<ProductsItem?> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllVendors(): Flow<ProductsResponse?> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllProductTypes(): Flow<ProductsResponse?> {
        TODO("Not yet implemented")
    }

    override suspend fun setInventory(
        locationId: Long,
        inventoryItemId: Long,
        available: Int
    ): Flow<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllPriceRules(): Flow<List<PriceRulesItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun updatePriceRule(id: Long, rule: PriceRulesItem): Flow<PriceRulesItem> {
        TODO("Not yet implemented")
    }

    override suspend fun createPriceRule(rule: PriceRulesItem): Flow<PriceRulesItem> {
        TODO("Not yet implemented")
    }

    override suspend fun deletePriceRule(priceRuleId: Long): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllDiscountCodes(): Flow<List<DiscountCode>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllPriceRulesSync(): List<PriceRulesItem> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteDiscountCode(
        priceRuleId: Long,
        discountCodeId: Long
    ): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updateDiscountCode(
        priceRuleId: Long,
        discountCodeId: Long,
        discountCode: DiscountCode
    ): Flow<DiscountCode> {
        TODO("Not yet implemented")
    }

    override suspend fun createDiscountCode(
        ruleId: Long,
        discountCode: DiscountCode
    ): Flow<DiscountCode> {
        TODO("Not yet implemented")
    }

    override suspend fun getInventoryItems(): Flow<List<InventoryItemUiModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun assignProductToCollection(
        productId: Long,
        collectionId: Long
    ): Flow<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProductFromAllCollections(productId: Long): Flow<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getCollectsForProduct(productId: Long): Flow<Long> {
        TODO("Not yet implemented")
    }
}