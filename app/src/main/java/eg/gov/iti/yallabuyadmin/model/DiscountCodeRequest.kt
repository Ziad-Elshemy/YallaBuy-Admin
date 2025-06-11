package eg.gov.iti.yallabuyadmin.model

import com.google.gson.annotations.SerializedName

data class DiscountCodeRequest(
    @SerializedName("discount_code")
    val discountCode: DiscountCode
)

data class DiscountCodeResponse(
    @SerializedName("discount_code")
    val discountCode: DiscountCode
)