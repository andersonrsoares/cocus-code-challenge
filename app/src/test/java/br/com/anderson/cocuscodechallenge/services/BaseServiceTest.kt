package br.com.anderson.cocuscodechallenge.services

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.anderson.cocuscodechallenge.extras.LanguageDeserializer
import br.com.anderson.cocuscodechallenge.model.Languages
import com.google.gson.GsonBuilder
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

open class BaseServiceTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    protected lateinit var service: CodeWarsService

    protected lateinit var mockWebServer: MockWebServer

    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        val gson = GsonBuilder().apply {
            registerTypeAdapter(Languages::class.java, LanguageDeserializer())
        }.create()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(CodeWarsService::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }
}
