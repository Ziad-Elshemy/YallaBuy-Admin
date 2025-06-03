package eg.gov.iti.yallabuyadmin.domain.usecase

import eg.gov.iti.yallabuyadmin.domain.model.Product
import eg.gov.iti.yallabuyadmin.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class GetAllProductsToAdmin(
    private val repository: Repository
) {
    suspend operator fun invoke(): Flow<List<Product>> {
        return repository.getAllProducts()
    }
}
