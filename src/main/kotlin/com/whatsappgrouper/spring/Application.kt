package com.whatsappgrouper.spring

import com.whatsappgrouper.spring.repository.api.WhatsappService
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

@SpringBootApplication
@EntityScan(basePackages = ["com.whatsappgrouper.spring.model"])
@EnableJpaRepositories(basePackages = ["com.whatsappgrouper.spring.model", "com.whatsappgrouper.spring.repository.db"])
class Application {

    @Value("\${com.whatsappgrouper.api-token}")
    lateinit var apiToken: String

    @Value("\${com.whatsappgrouper.api-base-path}")
    lateinit var apiBasePath: String

    @Bean
    fun retrofit(): Retrofit {
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.addInterceptor {chain ->
            val original = chain.request()
            val originalUrl = original.url()

            val url = originalUrl.newBuilder()
                    .addQueryParameter("token", apiToken)
                    .build()

            val requestBuilder = original.newBuilder().url(url)
            return@addInterceptor chain.proceed(requestBuilder.build())
        }

        val builder = Retrofit.Builder()
                .baseUrl(apiBasePath)
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())

        val retrofit = builder.build()
        return retrofit
    }

    @Bean
    fun whatsappService(retrofit: Retrofit): WhatsappService {
        val whatsappService = retrofit.create(WhatsappService::class.java)
        return whatsappService
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
