package eg.gov.iti.yallabuyadmin.editpricerule

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


class EditPriceRuleViewModel(private val repo: Repository) : ViewModel() {
    private val TAG = "EditPriceRuleViewModel"

    private val _updateState = MutableStateFlow<Response<PriceRulesItem>>(Response.Loading)
    val updateState = _updateState.asStateFlow()

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()


    fun updatePriceRule(id: Long, rule: PriceRulesItem) {
        viewModelScope.launch {
            _updateState.value = Response.Loading
            repo.updatePriceRule(id, rule)
                .catch { ex ->
                    _updateState.value = Response.Failure(ex)
                    _toastMessage.emit("Update Failed: ${ex.message}")
                }
                .collect {
                    _updateState.value = Response.Success(it)
                    _toastMessage.emit("Price Rule Updated")
                }
        }
    }

}

class EditPriceRuleFactory(private val repo: Repository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditPriceRuleViewModel(repo) as T
    }

}
