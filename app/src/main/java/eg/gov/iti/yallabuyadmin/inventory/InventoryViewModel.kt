package eg.gov.iti.yallabuyadmin.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eg.iti.mad.climaguard.repo.Repository


class InventoryViewModel(private val repo: Repository) : ViewModel() {
    private val TAG = "InventoryViewModel"

}

class InventoryFactory(private val repo: Repository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InventoryViewModel(repo) as T
    }

}
