package eg.gov.iti.yallabuyadmin.network.api

import eg.gov.iti.yallabuyadmin.model.AddImageRequest
import eg.gov.iti.yallabuyadmin.model.CreateProductRequest
import eg.gov.iti.yallabuyadmin.model.ImagesItem
import eg.gov.iti.yallabuyadmin.model.ProductByIdResponse
import eg.gov.iti.yallabuyadmin.model.ProductsItem
import eg.gov.iti.yallabuyadmin.model.ProductsResponse
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
    ): Response<ProductsItem>


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


}
