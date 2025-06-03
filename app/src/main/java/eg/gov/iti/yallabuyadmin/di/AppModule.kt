package eg.gov.iti.yallabuyadmin.di

import eg.gov.iti.yallabuyadmin.BuildConfig
import eg.gov.iti.yallabuyadmin.ui.coupons.CouponsViewModel
import eg.gov.iti.yallabuyadmin.ui.dashboard.DashboardViewModel
import eg.gov.iti.yallabuyadmin.data.repository.LocalDataSource
import eg.gov.iti.yallabuyadmin.data.datasource.local.LocalDataSourceImpl
import eg.gov.iti.yallabuyadmin.ui.inventory.InventoryViewModel
import eg.gov.iti.yallabuyadmin.data.repository.RemoteDataSource
import eg.gov.iti.yallabuyadmin.data.datasource.remote.RemoteDataSourceImpl
import eg.gov.iti.yallabuyadmin.data.datasource.remote.api.ShopifyApi
import eg.gov.iti.yallabuyadmin.ui.productdetails.ProductDetailsViewModel
import eg.gov.iti.yallabuyadmin.ui.products.ProductsViewModel
import eg.gov.iti.yallabuyadmin.ui.profile.ProfileViewModel
import eg.gov.iti.yallabuyadmin.domain.repository.Repository
import eg.gov.iti.yallabuyadmin.data.repository.RepositoryImpl
import eg.gov.iti.yallabuyadmin.domain.usecase.GetAllProductsToAdmin
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory




val appModule = module {

    single<HttpLoggingInterceptor> {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader(
                        "Authorization",
                        Credentials.basic(
                            BuildConfig.SHOPIFY_USERNAME,
                            BuildConfig.SHOPIFY_ACCESS_TOKEN
                        )
                    )
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://mad45-sv-and-01.myshopify.com/admin/api/2025-04/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<ShopifyApi> {
        get<Retrofit>().create(ShopifyApi::class.java)
    }


    factory <RemoteDataSource>{
        RemoteDataSourceImpl(get())
    }

//    single <ProductsDao>{
//        ProductsDataBase.getInstance(androidContext()).getProductsDao()
//    }
//
    factory <LocalDataSource>{
        LocalDataSourceImpl()
    }

    single <Repository>{
        RepositoryImpl.getInstance(get(),get())
    }

    single <GetAllProductsToAdmin>{
        GetAllProductsToAdmin(get())
    }


}



val viewModelsModule = module {

    viewModel <ProductsViewModel>{
        ProductsViewModel(get())
    }

    viewModel <InventoryViewModel> {
        InventoryViewModel(get())
    }

    viewModel <DashboardViewModel> {
        DashboardViewModel(get())
    }

    viewModel <CouponsViewModel> {
        CouponsViewModel(get())
    }

    viewModel <ProfileViewModel> {
        ProfileViewModel(get())
    }

    viewModel <ProductDetailsViewModel> {
        ProductDetailsViewModel(get())
    }

}