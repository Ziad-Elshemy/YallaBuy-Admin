package eg.iti.mad.climaguard.repo

import eg.gov.iti.yallabuyadmin.model.AddImageRequest
import eg.gov.iti.yallabuyadmin.model.DiscountCode
import eg.gov.iti.yallabuyadmin.model.ImagesItem
import eg.gov.iti.yallabuyadmin.model.InventoryItemUiModel
import eg.gov.iti.yallabuyadmin.model.PriceRulesItem
import eg.gov.iti.yallabuyadmin.model.ProductsItem
import eg.gov.iti.yallabuyadmin.model.ProductsResponse
import eg.gov.iti.yallabuyadmin.model.UpdateProductRequest
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun getAllProducts(): Flow<ProductsResponse>
    suspend fun deleteProduct(id: Long): Flow<Boolean>
    suspend fun getProductById(id: Long): Flow<ProductsItem?>

    suspend fun updateProduct(id: Long, productBody: UpdateProductRequest): Flow<ProductsItem?>
    suspend fun addProductImage(id: Long, imageBody: AddImageRequest): Flow<ImagesItem?>
    suspend fun deleteProductImage(productId: Long, imageId: Long): Flow<Unit?>
    suspend fun createProduct(product: ProductsItem): Flow<ProductsItem?>
    suspend fun getAllVendors(): Flow<ProductsResponse>
    suspend fun getAllProductTypes(): Flow<ProductsResponse>
    suspend fun setInventory(locationId: Long, inventoryItemId: Long, available: Int): Flow<Int>

    //price rules
    suspend fun getAllPriceRules(): Flow<List<PriceRulesItem>>
    suspend fun updatePriceRule(id: Long, rule: PriceRulesItem): Flow<PriceRulesItem>
    suspend fun createPriceRule(rule: PriceRulesItem): Flow<PriceRulesItem>
    suspend fun deletePriceRule(priceRuleId: Long): Flow<Boolean>

    //
    suspend fun getAllDiscountCodes(): Flow<List<DiscountCode>>
    suspend fun getAllPriceRulesSync(): List<PriceRulesItem>
    suspend fun deleteDiscountCode(priceRuleId: Long, discountCodeId: Long): Flow<Boolean>
    suspend fun updateDiscountCode(priceRuleId: Long, discountCodeId: Long, discountCode: DiscountCode): Flow<DiscountCode>
    suspend fun createDiscountCode(ruleId: Long, discountCode: DiscountCode): Flow<DiscountCode>


    suspend fun getInventoryItems(): Flow<List<InventoryItemUiModel>>


    suspend fun assignProductToCollection(productId: Long, collectionId: Long): Flow<Unit>

}