package eg.gov.iti.yallabuyadmin.network.api

import eg.gov.iti.yallabuyadmin.model.ProductsResponse
import retrofit2.http.GET

interface ShopifyApi {
    @GET("products.json")
    suspend fun getProducts(): ProductsResponse
}
