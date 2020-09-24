package br.com.anderson.cocuscodechallenge.di

import android.app.Application
import android.app.Service
import androidx.room.Room
import br.com.anderson.cocuscodechallenge.BuildConfig
import br.com.anderson.cocuscodechallenge.dto.Languages
import br.com.anderson.cocuscodechallenge.extras.AutorizationInterceptor
import br.com.anderson.cocuscodechallenge.extras.LanguageDeserializer
import br.com.anderson.cocuscodechallenge.services.CodewarsService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import javax.inject.Singleton

@Module
class AppModule {

    val URL = "https://www.codewars.com/api/v1/"

    @Singleton
    @Provides
    fun provideService(gson: Gson,okHttpClient: OkHttpClient): CodewarsService {
        return Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
            .create(CodewarsService::class.java)
    }


    @Singleton
    @Provides
    fun autorizationInterceptorProvider(): AutorizationInterceptor {
        return AutorizationInterceptor()
    }

    @Singleton
    @Provides
    fun okHttpClientCacheProvider(app: Application): Cache {
        return Cache(app.cacheDir, (5 * 1024 * 1024).toLong())
    }

    @Singleton
    @Provides
    fun okHttpClientProvider(autorizationInterceptor: AutorizationInterceptor): OkHttpClient{
        return OkHttpClient().newBuilder()
            .connectTimeout(6000, TimeUnit.MILLISECONDS)
            .readTimeout((1000 * 60).toLong(), TimeUnit.MILLISECONDS)
            .writeTimeout((1000 * 60).toLong(), TimeUnit.MILLISECONDS)
            .addInterceptor(autorizationInterceptor).apply {
                if (BuildConfig.DEBUG) {
                    val logInterceptor = HttpLoggingInterceptor()
                    logInterceptor.level = HttpLoggingInterceptor.Level.BODY
                    this.addInterceptor(logInterceptor)
                }
            }.build()
    }

    @Singleton
    @Provides
    fun gsonProvider() : Gson{
        return GsonBuilder().apply {
             registerTypeAdapter(Languages::class.java, LanguageDeserializer())
         }.create()
    }

}
