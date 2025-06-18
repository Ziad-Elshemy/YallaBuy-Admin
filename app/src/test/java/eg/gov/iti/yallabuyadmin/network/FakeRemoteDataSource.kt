package eg.gov.iti.yallabuyadmin.network

import eg.gov.iti.yallabuyadmin.model.AddImageRequest
import eg.gov.iti.yallabuyadmin.model.CollectItem
import eg.gov.iti.yallabuyadmin.model.DiscountCode
import eg.gov.iti.yallabuyadmin.model.ImagesItem
import eg.gov.iti.yallabuyadmin.model.PriceRulesItem
import eg.gov.iti.yallabuyadmin.model.PriceRulesResponse
import eg.gov.iti.yallabuyadmin.model.ProductsItem
import eg.gov.iti.yallabuyadmin.model.ProductsResponse
import eg.gov.iti.yallabuyadmin.model.UpdateProductRequest
import eg.gov.iti.yallabuyadmin.model.VariantsItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Response

class FakeRemoteDataSource(
    // Mock data for test 1
    private val fakeProductsResponse: ProductsResponse
) : RemoteDataSource {


    // Mock data for test 2
    private val priceRules = listOf(
        PriceRulesItem(id = 1L, title = "Rule 1"),
        PriceRulesItem(id = 2L, title = "Rule 2"),
    )

    private val discountsMap = mapOf(
        1L to listOf(
            DiscountCode(id = 100, code = "SAVE10", priceRuleId = 1L, usageCount = 0),
            DiscountCode(id = 101, code = "SAVE20", priceRuleId = 1L, usageCount = 3),
        ),
        2L to listOf(
            DiscountCode(id = 102, code = "HELLO5", priceRuleId = 2L, usageCount = 2),
        )
    )

    // Mock data for test 3
    var updatedProduct: ProductsItem? = null


    override suspend fun getAllProducts(): Flow<ProductsResponse> {
        return flowOf(fakeProductsResponse)
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
    ): Flow<ProductsItem?> = flow{
        updatedProduct = productBody.product
        emit(updatedProduct)
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

    override suspend fun getAllVendors(): Flow<ProductsResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllProductTypes(): Flow<ProductsResponse> {
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

    override suspend fun deletePriceRule(priceRuleId: Long): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getDiscountCodesByPriceRuleId(priceRuleId: Long): List<DiscountCode> {
        return discountsMap[priceRuleId] ?: emptyList()
    }

    override suspend fun getAllPriceRulesRaw(): Response<PriceRulesResponse> {
        return Response.success(PriceRulesResponse(priceRules.map { it }))
    }

    override suspend fun deleteDiscountCode(priceRuleId: Long, discountCodeId: Long): Boolean {
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

    override suspend fun getAllVariants(): List<VariantsItem> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllProductsForVariants(): List<ProductsItem> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllProductsWithVariants(): List<ProductsItem> {
        TODO("Not yet implemented")
    }

    override suspend fun assignProductToCollection(
        productId: Long,
        collectionId: Long
    ): Flow<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getCollectsForProduct(productId: Long): List<CollectItem> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCollect(collectId: Long) {
        TODO("Not yet implemented")
    }

}