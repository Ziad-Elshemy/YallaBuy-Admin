package eg.gov.iti.yallabuyadmin.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eg.iti.mad.climaguard.repo.Repository


class DashboardViewModel(private val repo: Repository) : ViewModel() {
    private val TAG = "DashboardViewModel"

}

class DashboardFactory(private val repo: Repository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DashboardViewModel(repo) as T
    }

}
