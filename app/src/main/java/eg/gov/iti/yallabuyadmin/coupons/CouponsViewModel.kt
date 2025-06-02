package eg.gov.iti.yallabuyadmin.coupons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eg.iti.mad.climaguard.repo.Repository


class CouponsViewModel(private val repo: Repository) : ViewModel() {
    private val TAG = "CouponsViewModel"

}

class CouponsFactory(private val repo: Repository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CouponsViewModel(repo) as T
    }

}
