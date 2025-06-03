package eg.gov.iti.yallabuyadmin.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eg.gov.iti.yallabuyadmin.domain.repository.Repository


class DashboardViewModel(private val repo: Repository) : ViewModel() {
    private val TAG = "DashboardViewModel"

}

class DashboardFactory(private val repo: Repository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DashboardViewModel(repo) as T
    }

}
