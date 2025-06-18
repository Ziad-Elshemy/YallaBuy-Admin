package eg.gov.iti.yallabuyadmin.network

import eg.gov.iti.yallabuyadmin.model.AddImageRequest
import eg.gov.iti.yallabuyadmin.model.CollectItem
import eg.gov.iti.yallabuyadmin.model.CreatePriceRuleRequest
import eg.gov.iti.yallabuyadmin.model.CreateProductRequest
import eg.gov.iti.yallabuyadmin.model.DiscountCode
import eg.gov.iti.yallabuyadmin.model.DiscountCodeRequest
import eg.gov.iti.yallabuyadmin.model.ImagesItem
import eg.gov.iti.yallabuyadmin.model.InventorySetRequest
import eg.gov.iti.yallabuyadmin.model.PriceRulesItem
import eg.gov.iti.yallabuyadmin.model.PriceRulesResponse
import eg.gov.iti.yallabuyadmin.model.ProductsItem
import eg.gov.iti.yallabuyadmin.model.ProductsResponse
import eg.gov.iti.yallabuyadmin.model.UpdatePriceRuleRequest
import eg.gov.iti.yallabuyadmin.model.UpdateProductRequest
import eg.gov.iti.yallabuyadmin.model.VariantsItem
import eg.gov.iti.yallabuyadmin.network.api.ShopifyApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Response

class RemoteDataSourceImpl(private val services: ShopifyApi): RemoteDataSource {
    override suspend fun getAllProducts(): Flow<ProductsResponse> {
        val response = services.getProducts()
        return flowOf(response)
    }

    override suspend fun deleteProduct(id: Long): Flow<Boolean> {
        val response = services.deleteProduct(id)
        return flowOf(response.isSuccessful)
    }

    override suspend fun getProductById(id: Long): Flow<ProductsItem?> {
        val response = services.getProductById(id)
        return flowOf(response.product)
    }

    override suspend fun updateProduct(
        id: Long,
        productBody: UpdateProductRequest
    ): Flow<ProductsItem?> {
        val response = services.updateProduct(id,productBody)
        if (response.isSuccessful) {
            return flowOf(response.body()?.product)
        } else {
            throw Exception("Update failed with code ${response.code()}")
        }
    }

    override suspend fun addProductImage(id: Long, imageBody: AddImageRequest): Flow<ImagesItem?> {
        val response = services.addProductImage(id,imageBody)
        if (response.isSuccessful){
            return flowOf(response.body())
        }
        else{
            throw Exception("Add image failed with code ${response.code()}")
        }
    }

    override suspend fun deleteProductImage(productId: Long, imageId: Long): Flow<Unit?> {
        val response = services.deleteProductImage(productId,imageId)
        if (response.isSuccessful){
            return flowOf(response.body())
        } else {
            throw Exception("Delete image failed with code ${response.code()}")
        }
    }

    override suspend fun getAllVendors(): Flow<ProductsResponse> {
        val response = services.getVendors()
        return flowOf(response)
    }

    override suspend fun getAllProductTypes(): Flow<ProductsResponse> {
        val response = services.getProductTypes()
        return flowOf(response)
    }

    override suspend fun setInventory(
        locationId: Long,
        inventoryItemId: Long,
        available: Int
    ): Flow<Int> = flow {
        val body = InventorySetRequest(
            locationId = locationId,
            inventoryItemId = inventoryItemId,
            available = available
        )
        val response = services.setInventory(body)
        if (response.isSuccessful) {
            emit(response.body()?.inventoryLevel?.available ?: 0)
        } else {
            throw Exception("Set inventory failed with code ${response.code()}")
        }
    }

    override suspend fun createProduct(product: ProductsItem): Flow<ProductsItem?> = flow{
        val response = services.createProduct(CreateProductRequest(product))
        if (response.isSuccessful){
            response.body()?.let { emit(it.product) }
        } else {
            throw Exception("Create product failed with code ${response.code()}")
        }
    }

    override suspend fun getAllPriceRules(): Flow<List<PriceRulesItem>> = flow {
        val response: Response<PriceRulesResponse> = services.getAllPriceRules()
        if (response.isSuccessful){
            val body = response.body()
            val rules = body?.priceRules?.filterNotNull() ?: emptyList()
            emit(rules)
        }else{
            throw Exception("Get Price Rules failed with code ${response.code()}")
        }
    }

    override suspend fun updatePriceRule(id: Long, rule: PriceRulesItem): Flow<PriceRulesItem> = flow {
        val response = services.updatePriceRule(id, UpdatePriceRuleRequest(rule))
        if (response.isSuccessful) {
            response.body()?.priceRule?.let { emit(it) }
        } else {
            throw Exception("Update failed: ${response.code()}")
        }
    }


    override suspend fun createPriceRule(rule: PriceRulesItem): Flow<PriceRulesItem> = flow {
        val response = services.createPriceRule(CreatePriceRuleRequest(rule))
        if (response.isSuccessful) {
            val created = response.body()?.priceRule
            if (created != null) emit(created)
            else throw Exception("Empty response from API")
        } else {
            throw Exception("Failed to create price rule: ${response.code()} ${response.message()}")
        }
    }

    override suspend fun deletePriceRule(priceRuleId: Long): Boolean {
        val response = services.deletePriceRule(priceRuleId)
        return response.isSuccessful
    }

    override suspend fun getDiscountCodesByPriceRuleId(priceRuleId: Long): List<DiscountCode> {
        val response = services.getDiscountCodes(priceRuleId)
        if (response.isSuccessful) {
            return response.body()?.discountCodes ?: emptyList()
        } else {
            throw Exception("Failed to fetch discount codes for rule $priceRuleId")
        }
    }

    override suspend fun getAllPriceRulesRaw(): Response<PriceRulesResponse> {
        return services.getAllPriceRules()
    }

    override suspend fun deleteDiscountCode(priceRuleId: Long, discountCodeId: Long): Boolean {
        val response = services.deleteDiscountCode(priceRuleId, discountCodeId)
        return response.isSuccessful
    }

    override suspend fun updateDiscountCode(
        priceRuleId: Long,
        discountCodeId: Long,
        discountCode: DiscountCode
    ): Flow<DiscountCode> = flow {
        val response = services.updateDiscountCode(priceRuleId,discountCodeId,
            DiscountCodeRequest(discountCode))
        if (response.isSuccessful) {
            val newDiscountCode = response.body()?.discountCode
            if (newDiscountCode != null) emit(newDiscountCode)
            else throw Exception("Empty response from API")
        }
    }

    override suspend fun createDiscountCode(ruleId: Long, discountCode: DiscountCode): Flow<DiscountCode> = flow {
        val response = services.createDiscountCode(ruleId, DiscountCodeRequest(discountCode))
        if (response.isSuccessful){
            val newDiscountCode = response.body()?.discountCode
            if (newDiscountCode != null) emit(newDiscountCode)
        }else{
            throw Exception("Empty response from API")
        }
    }


    override suspend fun getAllVariants(): List<VariantsItem> {
        val response = services.getAllVariants()
        if (response.isSuccessful) {
            return response.body()?.variants?.filterNotNull() ?: emptyList()
        } else {
            throw Exception("Failed to fetch variants: ${response.code()}")
        }
    }

    override suspend fun getAllProductsForVariants(): List<ProductsItem> {
        val response = services.getAllProducts()
        if (response.isSuccessful) {
            return response.body()?.products?.filterNotNull() ?: emptyList()
        } else {
            throw Exception("Failed to fetch products: ${response.code()}")
        }
    }

    override suspend fun getAllProductsWithVariants(): List<ProductsItem> {
        val response = services.getAllProductsWithVariants()
        if (response.isSuccessful) {
            return response.body()?.products?.filterNotNull() ?: emptyList()
        } else {
            throw Exception("Failed to fetch products with variants: ${response.code()}")
        }
    }


    override suspend fun assignProductToCollection(
        productId: Long,
        collectionId: Long
    ): Flow<Unit>  = flow {
        val body = mapOf(
            "collect" to mapOf(
                "product_id" to productId,
                "collection_id" to collectionId
            )
        )
        val response = services.assignToCollection(body)
        if (response.isSuccessful) emit(Unit)
        else throw Exception("Failed to assign to collection: ${response.code()}")
    }


    override suspend fun getCollectsForProduct(productId: Long): List<CollectItem> {
        val response = services.getCollects(productId)
        if (response.isSuccessful) {
            return response.body()?.collects ?: emptyList()
        } else {
            throw Exception("Failed to fetch collects for product $productId")
        }
    }

    override suspend fun deleteCollect(collectId: Long) {
        val response = services.deleteCollect(collectId)
        if (!response.isSuccessful) {
            throw Exception("Failed to delete collect with id $collectId")
        }
    }





}