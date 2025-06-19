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

class FakeInventoryRepository : Repository{

    private val fakeInventoryList = mutableListOf(
        InventoryItemUiModel(
            title = "T-Shirt",
            imageUrl = "https://image.url",
            variantTitle = "Red - M",
            inventoryItemId = 1L,
            quantity = 5,
            price = "19.99"
        ),
        InventoryItemUiModel(
            title = "Shoes",
            imageUrl = "https://image.url",
            variantTitle = "Black - 42",
            inventoryItemId = 2L,
            quantity = 3,
            price = "49.99"
        )
    )

    override suspend fun getInventoryItems(): Flow<List<InventoryItemUiModel>> {
        return flowOf(fakeInventoryList)
    }

    override suspend fun setInventory(
        locationId: Long,
        inventoryItemId: Long,
        available: Int
    ): Flow<Int> {
        val index = fakeInventoryList.indexOfFirst { it.inventoryItemId == inventoryItemId }
        if (index != -1) {
            fakeInventoryList[index] = fakeInventoryList[index].copy(quantity = available)
        }
        return flowOf(available)
    }

    override suspend fun getAllProducts(): Flow<ProductsResponse?> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProduct(id: Long): Flow<Boolean> {
        TODO("Not yet implemented")
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
