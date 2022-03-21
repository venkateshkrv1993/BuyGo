package com.vajro.buygo.dagger

import com.vajro.buygo.BuildConfig
import com.vajro.buygo.network.ApiServices
import com.vajro.buygo.network.MyCallAdapterFactory
import com.vajro.buygo.repository.ProductsRepository
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class Module {

    @Provides
    fun provideProductsRepository(): ProductsRepository {
        return ProductsRepository()
    }

    @Provides
    fun provideClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClientBuilder = OkHttpClient.Builder().addInterceptor(interceptor)
        okHttpClientBuilder.readTimeout(60, TimeUnit.SECONDS)
        okHttpClientBuilder.connectTimeout(60, TimeUnit.SECONDS)
        okHttpClientBuilder.cache(null)
        return okHttpClientBuilder.build()
    }

    @Provides
    fun provideRetrofit(baseURL: String, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .client(client)
            .addCallAdapterFactory(MyCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideApiService(): ApiServices {
        val retrofit = provideRetrofit(BuildConfig.BASE_URL, provideClient())
        return retrofit.create(ApiServices::class.java)
    }


}