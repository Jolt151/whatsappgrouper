package com.whatsappgrouper.spring

import com.whatsappgrouper.spring.repository.MessageRepository
import com.whatsappgrouper.spring.repository.api.WhatsappService
import com.whatsappgrouper.spring.repository.db.MessageDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.event.EventListener
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import kotlin.math.log


@SpringBootApplication
@EntityScan(basePackages = ["com.whatsappgrouper.spring.model"])
@EnableJpaRepositories(basePackages = ["com.whatsappgrouper.spring.model", "com.whatsappgrouper.spring.repository.db"])
class Application {

    val logger = KotlinLogging.logger{}

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

    @Bean
    fun runner(whatsappService: WhatsappService, messageDatabase: MessageDatabase, messageRepository: MessageRepository) = CommandLineRunner {
        runBlocking {
            GlobalScope.launch {
                logger.info { messageDatabase.getLatestMessageNumber() }

                messageRepository.sendMessage("13473597070@c.us", "testing send message ${System.currentTimeMillis()}")
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
