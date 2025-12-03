package com.tushar.data.datasource.remote.instrumentation

import com.tushar.data.keyprovider.KeyProvider
import com.tushar.data.keyprovider.SecureKeyProvider.Companion.KEY_COIN_AUTH
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenInterceptor @Inject constructor(private val keyProvider: KeyProvider) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = keyProvider[KEY_COIN_AUTH]?.let { key ->
            original.addAuthTokenHeader(key)
        } ?: original
        return chain.proceed(request)
    }

    private fun Request.addAuthTokenHeader(token: String) =
        newBuilder()
            .removeHeader(HEADER_AUTHORIZATION)
            .addHeader(HEADER_AUTHORIZATION, "Bearer $token")
            .build()

    companion object {
        const val HEADER_AUTHORIZATION = "Authorization"
    }
}
