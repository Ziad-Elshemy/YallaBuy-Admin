package eg.gov.iti.yallabuyadmin.ui.coupons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eg.gov.iti.yallabuyadmin.domain.repository.Repository


class CouponsViewModel(private val repo: Repository) : ViewModel() {
    private val TAG = "CouponsViewModel"

}

class CouponsFactory(private val repo: Repository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CouponsViewModel(repo) as T
    }

}
