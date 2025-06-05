package eg.gov.iti.yallabuyadmin.network

import eg.gov.iti.yallabuyadmin.model.AddImageRequest
import eg.gov.iti.yallabuyadmin.model.ImagesItem
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
}