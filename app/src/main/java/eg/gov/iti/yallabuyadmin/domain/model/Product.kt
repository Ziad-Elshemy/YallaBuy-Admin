package eg.gov.iti.yallabuyadmin.domain.model


data class Product(
    val id: Long,
    val title: String,
    val imageUrl: String,
    val productType: String,
    val price: Double,
    val quantity: Int
)

