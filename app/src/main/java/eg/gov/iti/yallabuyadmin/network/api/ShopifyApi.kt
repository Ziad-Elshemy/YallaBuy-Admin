package eg.gov.iti.yallabuyadmin.network.api

import eg.gov.iti.yallabuyadmin.model.ProductsItem
import eg.gov.iti.yallabuyadmin.model.ProductsResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface ShopifyApi {
    @GET("products.json")
    suspend fun getProducts(): ProductsResponse


    // delete products
    @DELETE("products/{id}.json")
    suspend fun deleteProduct(@Path("id") productId: Long): Response<Unit>


    @GET("products/{id}.json")
    suspend fun getProductById(@Path("id") productId: Long): ProductsItem

}
