package eg.gov.iti.yallabuyadmin.network.api

import eg.gov.iti.yallabuyadmin.model.AddImageRequest
import eg.gov.iti.yallabuyadmin.model.CreateProductRequest
import eg.gov.iti.yallabuyadmin.model.ImagesItem
import eg.gov.iti.yallabuyadmin.model.InventorySetRequest
import eg.gov.iti.yallabuyadmin.model.InventoryLevelResponse
import eg.gov.iti.yallabuyadmin.model.PriceRuleResponseWrapper
import eg.gov.iti.yallabuyadmin.model.PriceRulesResponse
import eg.gov.iti.yallabuyadmin.model.ProductByIdResponse
import eg.gov.iti.yallabuyadmin.model.ProductWrapper
import eg.gov.iti.yallabuyadmin.model.ProductsItem
import eg.gov.iti.yallabuyadmin.model.ProductsResponse
import eg.gov.iti.yallabuyadmin.model.UpdatePriceRuleRequest
import eg.gov.iti.yallabuyadmin.model.UpdateProductRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ShopifyApi {
    @GET("products.json")
    suspend fun getProducts(): ProductsResponse


    // delete products
    @DELETE("products/{id}.json")
    suspend fun deleteProduct(@Path("id") productId: Long): Response<Unit>


    @GET("products/{id}.json")
    suspend fun getProductById(
        @Path("id") productId: Long
    ): ProductByIdResponse


    // update product
    @PUT("products/{id}.json")
    suspend fun updateProduct(
        @Path("id") productId: Long,
        @Body productBody: UpdateProductRequest
    ): Response<ProductWrapper>


    // add image to product
    @POST("products/{product_id}/images.json")
    suspend fun addProductImage(
        @Path("product_id") productId: Long,
        @Body request: AddImageRequest
    ): Response<ImagesItem>


    // delete product image
    @DELETE("products/{product_id}/images/{image_id}.json")
    suspend fun deleteProductImage(
        @Path("product_id") productId: Long,
        @Path("image_id") imageId: Long
    ): Response<Unit>

    //create product
    @POST("products.json")
    suspend fun createProduct(
        @Body request: CreateProductRequest
    ): Response<ProductsItem>

    @GET("products.json?fields=vendor")
    suspend fun getVendors(): ProductsResponse

    @GET("products.json?fields=product_type")
    suspend fun getProductTypes(): ProductsResponse

    @POST("inventory_levels/set.json")
    suspend fun setInventory(
        @Body body: InventorySetRequest
    ): Response<InventoryLevelResponse>


    @GET("price_rules.json")
    suspend fun getAllPriceRules(): Response<PriceRulesResponse>

    @PUT("price_rules/{id}.json")
    suspend fun updatePriceRule(
        @Path("id") ruleId: Long,
        @Body body: UpdatePriceRuleRequest
    ): Response<PriceRuleResponseWrapper>


}
