package eg.gov.iti.yallabuyadmin.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eg.gov.iti.yallabuyadmin.domain.repository.Repository


class ProfileViewModel(private val repo: Repository) : ViewModel() {
    private val TAG = "ProfileViewModel"

}

class ProfileFactory(private val repo: Repository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(repo) as T
    }

}
