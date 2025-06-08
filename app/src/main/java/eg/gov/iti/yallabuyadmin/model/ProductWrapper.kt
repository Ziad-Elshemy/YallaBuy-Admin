package eg.gov.iti.yallabuyadmin.model

import com.google.gson.annotations.SerializedName

data class ProductWrapper(
    @SerializedName("product") val product: ProductsItem?
)