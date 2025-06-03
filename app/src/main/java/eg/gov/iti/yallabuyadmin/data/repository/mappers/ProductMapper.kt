package eg.gov.iti.yallabuyadmin.data.repository.mappers

import eg.gov.iti.yallabuyadmin.data.dto.ProductsItem
import eg.gov.iti.yallabuyadmin.data.dto.ProductsResponseDto
import eg.gov.iti.yallabuyadmin.domain.model.Product

fun ProductsItem.toProduct(): Product{
    return Product(
        id = this.id ?: 0L,
        title = this.title.orEmpty(),
        imageUrl = this.image?.src.orEmpty(),
        productType = this.productType.orEmpty(),
        price = this.variants?.firstOrNull()?.price?.toDoubleOrNull() ?: 0.0,
        quantity = this.variants?.firstOrNull()?.inventoryQuantity ?: 0
    )
}