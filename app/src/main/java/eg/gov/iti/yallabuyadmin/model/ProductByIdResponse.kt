package eg.gov.iti.yallabuyadmin.model

import com.google.gson.annotations.SerializedName

data class ProductByIdResponse(
    @SerializedName("product")
    val product: ProductsItem?
)
