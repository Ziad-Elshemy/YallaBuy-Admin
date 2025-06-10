package eg.gov.iti.yallabuyadmin.createpricerule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import eg.gov.iti.yallabuyadmin.model.PriceRulesItem
import eg.gov.iti.yallabuyadmin.model.Response
import eg.iti.mad.climaguard.repo.Repository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class CreatePriceRuleViewModel(private val repo: Repository) : ViewModel() {

    private val _createState = MutableStateFlow<Response<PriceRulesItem>>(Response.Loading)
    val createState = _createState.asStateFlow()

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    fun createPriceRule(rule: PriceRulesItem) {
        viewModelScope.launch {
            _createState.value = Response.Loading
            repo.createPriceRule(rule)
                .catch { ex ->
                    _createState.value = Response.Failure(ex)
                    _toastMessage.emit("Create Failed: ${ex.message}")
                }
                .collect {
                    _createState.value = Response.Success(it)
                    _toastMessage.emit("Price Rule Created")
                }
        }
    }
}

