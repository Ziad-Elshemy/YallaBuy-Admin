package eg.gov.iti.yallabuyadmin.model

import com.google.gson.annotations.SerializedName

data class DiscountCodesResponse(
    @SerializedName("discount_codes")
    val discountCodes: List<DiscountCode>? = null
)

data class DiscountCode(
    @SerializedName("id")
    val id: Long? = null,

    @SerializedName("code")
    val code: String? = null,

    @SerializedName("price_rule_id")
    val priceRuleId: Long? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null,

    @SerializedName("usage_count")
    val usageCount: Int? = null
)
