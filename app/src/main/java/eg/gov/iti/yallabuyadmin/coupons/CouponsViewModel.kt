package eg.gov.iti.yallabuyadmin.coupons

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import eg.gov.iti.yallabuyadmin.model.DiscountCode
import eg.gov.iti.yallabuyadmin.model.DiscountCodeRequest
import eg.gov.iti.yallabuyadmin.model.PriceRulesItem
import eg.gov.iti.yallabuyadmin.model.Response
import eg.iti.mad.climaguard.repo.Repository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class CouponsViewModel(private val repo: Repository) : ViewModel() {
    private val TAG = "CouponsViewModel"

    private val _priceRules = MutableStateFlow<Response<List<PriceRulesItem>>>(Response.Loading)
    val priceRules = _priceRules.asStateFlow()

    private val _discountCodes = MutableStateFlow<Response<List<DiscountCode>>>(Response.Loading)
    val discountCodes = _discountCodes.asStateFlow()

    private val _deleteResult = MutableSharedFlow<String>()
    val deleteResult = _deleteResult.asSharedFlow()

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    fun fetchPriceRules() {
        viewModelScope.launch {
            repo.getAllPriceRules()
                .catch {
                    _priceRules.value = Response.Failure(it)
                    Log.e(TAG, "fetchPriceRules: Error = $it", )
                }
                .collect { _priceRules.value = Response.Success(it) }
        }
    }


    fun fetchDiscountCodes() {
        viewModelScope.launch {
            repo.getAllDiscountCodes()
                .catch {
                    _discountCodes.value = Response.Failure(it)
                    Log.e(TAG, "fetchDiscountCodes: Error = $it")
                }
                .collect {
                    _discountCodes.value = Response.Success(it)
                }
        }
    }

    fun deletePriceRule(priceRuleId: Long) {
        viewModelScope.launch {
            repo.deletePriceRule(priceRuleId)
                .catch { _deleteResult.emit("Failed to delete: ${it.message}") }
                .collect { success ->
                    if (success) {
                        _deleteResult.emit("Rule deleted successfully")
                        fetchPriceRules()
                    } else {
                        _deleteResult.emit("Deletion failed")
                    }
                }
        }
    }


    fun deleteDiscountCode(priceRuleId: Long, discountCodeId: Long) {
        viewModelScope.launch {
            repo.deleteDiscountCode(priceRuleId, discountCodeId)
                .catch { _deleteResult.emit("Failed to delete: ${it.message}") }
                .collect { success ->
                    if (success) {
                        _deleteResult.emit("Discount deleted successfully")
                        fetchDiscountCodes()
                    } else {
                        _deleteResult.emit("Deletion failed")
                    }
                }
        }
    }

    fun updateDiscountCode(priceRuleId: Long, discountId: Long, newCode: String) {
        viewModelScope.launch {
            try {
                repo.updateDiscountCode(
                    priceRuleId,
                    discountId,
                    DiscountCode(
                        id = discountId,
                        priceRuleId = priceRuleId,
                        code = newCode
                    )
                )
                    .catch { e ->
                        _toastMessage.emit("Update failed: ${e.message}")
                    }
                    .collect{ discount ->
                        _toastMessage.emit("Discount ${discount.code} updated")
                        fetchDiscountCodes()
                    }

            } catch (e: Exception) {
                _toastMessage.emit("Update failed: ${e.message}")
            }
        }
    }



}

class CouponsFactory(private val repo: Repository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CouponsViewModel(repo) as T
    }

}
