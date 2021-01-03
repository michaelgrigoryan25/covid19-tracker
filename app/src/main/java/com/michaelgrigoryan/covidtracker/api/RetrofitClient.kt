package com.michaelgrigoryan.covidtracker.api

import com.google.gson.GsonBuilder
import com.michaelgrigoryan.covidtracker.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {

    companion object {
        private lateinit var apiService: APIService
        private var retrofitClient: RetrofitClient? = null

        fun getRetrofit(): RetrofitClient {
            if (retrofitClient == null) {
                synchronized(this) {
                    retrofitClient = RetrofitClient()
                }
            }
            return retrofitClient!!
        }
    }

    private fun headerInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request().newBuilder()
                    .header("x-rapidapi-key", "9058ab254bmsh06db2fa7427a4e6p1013e4jsn2217831e6a2e")
                    .header("x-rapidapi-host", "covid-193.p.rapidapi.com")
                    .header("useQueryString", "true")
                    .build()
            chain.proceed(request)
        }
    }

    init {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(0, TimeUnit.SECONDS)
                .readTimeout(0, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .apply {
                    if (BuildConfig.DEBUG) {
                        this.addInterceptor(loggingInterceptor)
                    }
                    this.addInterceptor(headerInterceptor())
                }.build()

        val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
        apiService = retrofit.create(APIService::class.java)
    }

    fun getAPIService() = apiService
}