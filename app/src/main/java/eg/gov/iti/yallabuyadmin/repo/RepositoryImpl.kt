package eg.iti.mad.climaguard.repo

import eg.gov.iti.yallabuyadmin.database.LocalDataSource
import eg.gov.iti.yallabuyadmin.model.ProductsResponse
import eg.gov.iti.yallabuyadmin.network.RemoteDataSource
import kotlinx.coroutines.flow.Flow

class RepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
): Repository {

    private val TAG = "RepositoryImpl"


    override suspend fun getAllProducts(): Flow<ProductsResponse> {
        return remoteDataSource.getAllProducts()
    }




    companion object {
        private var INSTANCE: RepositoryImpl? = null
        fun getInstance(localDataSource: LocalDataSource, remoteDataSource: RemoteDataSource): RepositoryImpl{
            return INSTANCE ?: synchronized(this){
                val instance = RepositoryImpl(localDataSource, remoteDataSource)
                INSTANCE = instance
                instance
            }
        }
    }

}