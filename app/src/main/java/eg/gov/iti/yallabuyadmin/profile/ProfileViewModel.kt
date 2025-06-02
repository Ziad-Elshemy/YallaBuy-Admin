package eg.gov.iti.yallabuyadmin.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eg.iti.mad.climaguard.repo.Repository


class ProfileViewModel(private val repo: Repository) : ViewModel() {
    private val TAG = "ProfileViewModel"

}

class ProfileFactory(private val repo: Repository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(repo) as T
    }

}
