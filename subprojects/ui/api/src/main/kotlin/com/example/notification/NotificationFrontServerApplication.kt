package com.example.notification

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication(scanBasePackages = ["com.example.notification"])
@Configuration
@ConfigurationPropertiesScan
@EnableScheduling
@EntityScan(basePackages = ["com.example.notification.domain"])
@EnableJpaRepositories(basePackages = ["com.example.notification.domain"])
open class NotificationFrontServerApplication

fun main(args: Array<String>) {
    runApplication<NotificationFrontServerApplication>(*args)
}

