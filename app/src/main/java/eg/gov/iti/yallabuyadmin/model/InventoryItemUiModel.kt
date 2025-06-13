package eg.gov.iti.yallabuyadmin.model

data class InventoryItemUiModel(
    val title: String,
    val imageUrl: String?,
    val variantTitle: String,
    val inventoryItemId: Long,
    val quantity: Int,
    val price: String
)
