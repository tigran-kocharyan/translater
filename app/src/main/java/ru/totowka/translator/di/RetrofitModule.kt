package ru.totowka.translator.di

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.totowka.translator.BuildConfig
import ru.totowka.translator.data.api.SkyEngApi
import javax.inject.Singleton


@Module
class RetrofitModule {
    @Provides
    @Singleton
    fun provideSkyEngApi(builder: Retrofit.Builder): SkyEngApi = builder
        .baseUrl("$TRANSLATOR_BASE_URL")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(SkyEngApi::class.java)

    @Provides
    @Singleton
    fun provideRetrofitBuilder(client: OkHttpClient): Retrofit.Builder = Retrofit.Builder()
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())

    @Provides
    @Singleton
    fun provideOkHttp(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient = OkHttpClient()
        .newBuilder()
        .addInterceptor(httpLoggingInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)

    companion object {
        private const val TRANSLATOR_BASE_URL = "https://dictionary.skyeng.ru/"
    }
}