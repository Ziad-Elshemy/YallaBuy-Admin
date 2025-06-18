package eg.gov.iti.yallabuyadmin.creatediscount

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import eg.gov.iti.yallabuyadmin.model.DiscountCode
import eg.gov.iti.yallabuyadmin.model.PriceRulesItem
import eg.gov.iti.yallabuyadmin.model.Response
import eg.iti.mad.climaguard.repo.Repository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class CreateDiscountViewModel(private val repo: Repository) : ViewModel() {
    private val TAG = "CreateDiscountViewModel"

    private val _priceRules = MutableStateFlow<Response<List<PriceRulesItem>>>(Response.Loading)
    val priceRules = _priceRules.asStateFlow()

    private val _createState = MutableStateFlow<Response<DiscountCode>>(Response.Loading)
    val createState = _createState.asStateFlow()


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


    fun createDiscountCode(ruleId: Long, discountCode: DiscountCode) {
        viewModelScope.launch {
            _createState.value = Response.Loading
            repo.createDiscountCode(ruleId, discountCode)
                .catch { e ->
                    _createState.value = Response.Failure(e)
                    _toastMessage.emit("Creation failed: ${e.message}")
                }
                .collect { createdDiscount ->
                    _createState.value = Response.Success(createdDiscount)
                    _toastMessage.emit("Discount created")
                }
        }
    }




}

