package eg.gov.iti.yallabuyadmin.addproduct

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eg.iti.mad.climaguard.repo.Repository


class CreateProductViewModel(private val repo: Repository) : ViewModel() {
    private val TAG = "CreateProductViewModel"

}

class CreateProductFactory(private val repo: Repository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CreateProductViewModel(repo) as T
    }

}
