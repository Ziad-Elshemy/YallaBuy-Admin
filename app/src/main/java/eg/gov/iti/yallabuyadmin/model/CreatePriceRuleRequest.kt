package eg.gov.iti.yallabuyadmin.model

import com.google.gson.annotations.SerializedName

data class CreatePriceRuleRequest(
    @SerializedName("price_rule")
    val priceRule: PriceRulesItem
)

data class CreatePriceRuleResponse(
    @SerializedName("price_rule")
    val priceRule: PriceRulesItem
)
