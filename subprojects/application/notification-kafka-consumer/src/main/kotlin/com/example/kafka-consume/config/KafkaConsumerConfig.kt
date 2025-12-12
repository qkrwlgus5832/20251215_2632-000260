package com.example.`kafka-consume`.config

import com.example.notification.infra.kafka.properties.KafkaProperties
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory

@Configuration
@EnableKafka
@ConfigurationPropertiesScan(basePackages = ["com.example.notification"])
class KafkaConsumerConfig(
    private val kafkaProperties: KafkaProperties,
) {
    @Bean
    fun consumerFactory(): ConsumerFactory<String, String> =
        DefaultKafkaConsumerFactory(
            mapOf(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaProperties.bootstrapServers,
                ConsumerConfig.GROUP_ID_CONFIG to kafkaProperties.notification.consumer.groupId,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java
            )
        )

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> {
        val concurrencyCount = kafkaProperties.notification.consumer.concurrencyCount
        return ConcurrentKafkaListenerContainerFactory<String, String>().apply {
            consumerFactory = consumerFactory()
            setConcurrency(concurrencyCount.toInt())
        }
    }
}