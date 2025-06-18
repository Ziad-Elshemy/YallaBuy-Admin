package eg.gov.iti.yallabuyadmin.model

data class CollectsResponse(
    val collects: List<CollectItem>
)

data class CollectItem(
    val id: Long,
    val collection_id: Long,
    val product_id: Long,
    val created_at: String?,
    val updated_at: String?,
    val position: Int,
    val sort_value: String
)
