package eg.gov.iti.yallabuyadmin.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PriceRulesResponse(

	@field:SerializedName("price_rules")
	val priceRules: List<PriceRulesItem?>? = null
)

data class PriceRulesItem(

	@field:SerializedName("value_type")
	val valueType: String? = null,

	@field:SerializedName("once_per_customer")
	val oncePerCustomer: Boolean? = null,

	@field:SerializedName("usage_limit")
	val usageLimit: String? = null,

	@field:SerializedName("starts_at")
	val startsAt: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("prerequisite_customer_ids")
	val prerequisiteCustomerIds: List<Any?>? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("entitled_collection_ids")
	val entitledCollectionIds: List<Any?>? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("prerequisite_product_ids")
	val prerequisiteProductIds: List<Any?>? = null,

	@field:SerializedName("prerequisite_shipping_price_range")
	val prerequisiteShippingPriceRange: Any? = null,

	@field:SerializedName("entitled_country_ids")
	val entitledCountryIds: List<Any?>? = null,

	@field:SerializedName("entitled_variant_ids")
	val entitledVariantIds: List<Any?>? = null,

	@field:SerializedName("id")
	val id: Long? = null,

	@field:SerializedName("ends_at")
	val endsAt: String? = null,

	@field:SerializedName("value")
	val value: String? = null,

	@field:SerializedName("prerequisite_subtotal_range")
	val prerequisiteSubtotalRange: Any? = null,

	@field:SerializedName("allocation_method")
	val allocationMethod: String? = null,

	@field:SerializedName("prerequisite_to_entitlement_quantity_ratio")
	val prerequisiteToEntitlementQuantityRatio: PrerequisiteToEntitlementQuantityRatio? = null,

	@field:SerializedName("prerequisite_quantity_range")
	val prerequisiteQuantityRange: Any? = null,

	@field:SerializedName("allocation_limit")
	val allocationLimit: Any? = null,

	@field:SerializedName("target_type")
	val targetType: String? = null,

	@field:SerializedName("entitled_product_ids")
	val entitledProductIds: List<Any?>? = null,

	@field:SerializedName("customer_segment_prerequisite_ids")
	val customerSegmentPrerequisiteIds: List<Any?>? = null,

	@field:SerializedName("customer_selection")
	val customerSelection: String? = null,

	@field:SerializedName("prerequisite_variant_ids")
	val prerequisiteVariantIds: List<Any?>? = null,

	@field:SerializedName("admin_graphql_api_id")
	val adminGraphqlApiId: String? = null,

	@field:SerializedName("target_selection")
	val targetSelection: String? = null,

	@field:SerializedName("prerequisite_to_entitlement_purchase")
	val prerequisiteToEntitlementPurchase: PrerequisiteToEntitlementPurchase? = null,

	@field:SerializedName("prerequisite_collection_ids")
	val prerequisiteCollectionIds: List<Any?>? = null
) : Serializable

data class PrerequisiteToEntitlementPurchase(

	@field:SerializedName("prerequisite_amount")
	val prerequisiteAmount: Any? = null
)

data class PrerequisiteToEntitlementQuantityRatio(

	@field:SerializedName("prerequisite_quantity")
	val prerequisiteQuantity: Any? = null,

	@field:SerializedName("entitled_quantity")
	val entitledQuantity: Any? = null
)
