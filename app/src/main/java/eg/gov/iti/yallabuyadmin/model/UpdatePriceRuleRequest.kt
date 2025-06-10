package eg.gov.iti.yallabuyadmin.model

import com.google.gson.annotations.SerializedName

data class UpdatePriceRuleRequest(
    @SerializedName("price_rule")
    val priceRule: PriceRulesItem
)

data class PriceRuleResponseWrapper(
    @SerializedName("price_rule")
    val priceRule: PriceRulesItem
)
