package eg.gov.iti.yallabuyadmin.network

import eg.gov.iti.yallabuyadmin.model.AddImageRequest
import eg.gov.iti.yallabuyadmin.model.ImagesItem
import eg.gov.iti.yallabuyadmin.model.PriceRulesItem
import eg.gov.iti.yallabuyadmin.model.ProductsItem
import eg.gov.iti.yallabuyadmin.model.ProductsResponse
import eg.gov.iti.yallabuyadmin.model.UpdateProductRequest
import kotlinx.coroutines.flow.Flow

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

}