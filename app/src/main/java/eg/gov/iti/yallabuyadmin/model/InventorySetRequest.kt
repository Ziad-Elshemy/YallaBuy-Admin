package eg.gov.iti.yallabuyadmin.model

import com.google.gson.annotations.SerializedName

data class InventorySetRequest(
    @SerializedName("location_id") val locationId: Long,
    @SerializedName("inventory_item_id") val inventoryItemId: Long,
    @SerializedName("available") val available: Int
)

data class InventoryLevelResponse(
    @SerializedName("inventory_level") val inventoryLevel: InventoryLevel
)

data class InventoryLevel(
    @SerializedName("inventory_item_id") val inventoryItemId: Long,
    @SerializedName("location_id") val locationId: Long,
    @SerializedName("available") val available: Int
)
