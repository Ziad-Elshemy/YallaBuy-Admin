package eg.gov.iti.yallabuyadmin.model

data class ProductsResponse(
    val products: List<Product>
)

data class Product(
    val id: Long,
    val title: String,
    val body_html: String?,
    val vendor: String
)
