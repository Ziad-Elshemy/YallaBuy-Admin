package eg.gov.iti.yallabuyadmin.network

import eg.gov.iti.yallabuyadmin.model.AddImageRequest
import eg.gov.iti.yallabuyadmin.model.DiscountCode
import eg.gov.iti.yallabuyadmin.model.ImagesItem
import eg.gov.iti.yallabuyadmin.model.PriceRulesItem
import eg.gov.iti.yallabuyadmin.model.PriceRulesResponse
import eg.gov.iti.yallabuyadmin.model.ProductsItem
import eg.gov.iti.yallabuyadmin.model.ProductsResponse
import eg.gov.iti.yallabuyadmin.model.UpdateProductRequest
import eg.gov.iti.yallabuyadmin.model.VariantsItem
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RemoteDataSource {

    suspend fun getAllProducts(): Flow<ProductsResponse>
    suspend fun deleteProduct(id: Long): Flow<Boolean>
    suspend fun getProductById(id: Long): Flow<ProductsItem?>

    suspend fun updateProduct(id: Long, productBody: UpdateProductRequest): Flow<ProductsItem?>
    suspend fun addProductImage(id: Long, imageBody: AddImageRequest): Flow<ImagesItem?>
    suspend fun deleteProductImage(productId: Long, imageId: Long): Flow<Unit?>
    suspend fun createProduct(product: ProductsItem): Flow<ProductsItem>
    suspend fun getAllVendors(): Flow<ProductsResponse>
    suspend fun getAllProductTypes(): Flow<ProductsResponse>
    suspend fun setInventory(locationId: Long, inventoryItemId: Long, available: Int): Flow<Int>

    //price rules
    suspend fun getAllPriceRules(): Flow<List<PriceRulesItem>>
    suspend fun updatePriceRule(id: Long, rule: PriceRulesItem): Flow<PriceRulesItem>
    suspend fun createPriceRule(rule: PriceRulesItem): Flow<PriceRulesItem>
    suspend fun deletePriceRule(priceRuleId: Long): Boolean

    //
    suspend fun getDiscountCodesByPriceRuleId(priceRuleId: Long): List<DiscountCode>
    suspend fun getAllPriceRulesRaw(): Response<PriceRulesResponse>
    suspend fun deleteDiscountCode(priceRuleId: Long, discountCodeId: Long): Boolean
    suspend fun updateDiscountCode(priceRuleId: Long, discountCodeId: Long, discountCode: DiscountCode): Flow<DiscountCode>
    suspend fun createDiscountCode(ruleId: Long, discountCode: DiscountCode): Flow<DiscountCode>


    suspend fun getAllVariants(): List<VariantsItem>
    suspend fun getAllProductsForVariants(): List<ProductsItem>



}