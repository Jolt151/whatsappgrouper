package com.whatsappgrouper.spring

import com.test.model.Message
import com.whatsappgrouper.spring.repository.db.MessageDatabase
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import javax.persistence.Entity

@SpringBootApplication
@EntityScan(basePackages = ["com.whatsappgrouper.spring.model"])
@EnableJpaRepositories(basePackages = ["com.whatsappgrouper.spring.model"])
class Application {
    
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
