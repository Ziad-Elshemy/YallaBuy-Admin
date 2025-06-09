package eg.gov.iti.yallabuyadmin.di

import eg.gov.iti.yallabuyadmin.BuildConfig
import eg.gov.iti.yallabuyadmin.addproduct.CreateProductViewModel
import eg.gov.iti.yallabuyadmin.coupons.CouponsViewModel
import eg.gov.iti.yallabuyadmin.dashboard.DashboardViewModel
import eg.gov.iti.yallabuyadmin.database.LocalDataSource
import eg.gov.iti.yallabuyadmin.database.LocalDataSourceImpl
import eg.gov.iti.yallabuyadmin.editpricerule.EditPriceRuleViewModel
import eg.gov.iti.yallabuyadmin.inventory.InventoryViewModel
import eg.gov.iti.yallabuyadmin.network.RemoteDataSource
import eg.gov.iti.yallabuyadmin.network.RemoteDataSourceImpl
import eg.gov.iti.yallabuyadmin.network.api.ShopifyApi
import eg.gov.iti.yallabuyadmin.productdetails.ProductDetailsViewModel
import eg.gov.iti.yallabuyadmin.products.ProductsViewModel
import eg.gov.iti.yallabuyadmin.profile.ProfileViewModel
import eg.iti.mad.climaguard.repo.Repository
import eg.iti.mad.climaguard.repo.RepositoryImpl
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
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

    viewModel <CreateProductViewModel> {
        CreateProductViewModel(get())
    }

    viewModel <EditPriceRuleViewModel> {
        EditPriceRuleViewModel(get())
    }

}