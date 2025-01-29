package org.coroutines.integrator.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.coroutines.integrator.entities.PackageStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ContainerProperties


@EnableKafka
@Configuration
class Configuration(
    @Value("\${spring.kafka.bootstrap-servers}") private val bootstrapServers: String,
    @Value("\${spring.kafka.consumer.group-id}") private val groupId: String,
    @Value("\${spring.kafka.consumer.auto-offset-reset}") private val autoOffsetReset: String
) {

    @Bean
    fun consumerFactory(): ConsumerFactory<String, PackageStatus> {
        val props: MutableMap<String, Any> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        props[ConsumerConfig.GROUP_ID_CONFIG] = groupId
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = PackageStatusDeserializer::class.java
        props[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = autoOffsetReset

        return DefaultKafkaConsumerFactory(props)
    }

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, PackageStatus> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, PackageStatus>()
        factory.consumerFactory = consumerFactory()
        factory.containerProperties.ackMode = ContainerProperties.AckMode.MANUAL_IMMEDIATE
        factory.containerProperties.isSyncCommits = true;

        return factory
    }
}