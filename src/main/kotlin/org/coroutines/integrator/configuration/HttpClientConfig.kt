package org.coroutines.integrator.configuration

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import org.coroutines.integrator.types.StatusDataSource
import org.coroutines.integrator.infra.OkHttpStatusDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class HttpClientConfig {
    @Bean
    fun okHttpClient(): OkHttpClient = OkHttpClient()

    @Bean
    fun jsonMediaType(): MediaType? = "application/json".toMediaTypeOrNull()

    @Bean
    fun statusDataSource(
        okHttpClient: OkHttpClient,
        jsonMediaType: MediaType?
    ): StatusDataSource = OkHttpStatusDataSource(okHttpClient, jsonMediaType)
}