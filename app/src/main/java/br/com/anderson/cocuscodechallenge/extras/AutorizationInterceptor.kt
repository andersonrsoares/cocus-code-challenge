package br.com.anderson.cocuscodechallenge.extras

import okhttp3.Interceptor
import okhttp3.Response

val AUTORIZATION = "Q39qgF5UvxcP9UQeN3-Q"

class AutorizationInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        request.addHeader("Authorization", AUTORIZATION)
        return chain.proceed(request.build())
    }
}

