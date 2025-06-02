package eg.gov.iti.yallabuyadmin.network.api

import eg.gov.iti.yallabuyadmin.BuildConfig
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://mad45-sv-and-01.myshopify.com/admin/api/2025-04/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader(
                    "Authorization",
                    Credentials.basic(
                        BuildConfig.SHOPIFY_USERNAME, // API Key
                        BuildConfig.SHOPIFY_ACCESS_TOKEN // Password
                    )
                )
                .build()
            chain.proceed(request)
        }
        .build()

    val api: ShopifyApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ShopifyApi::class.java)
    }
}
