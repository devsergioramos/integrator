package org.coroutines.integrator.configuration

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class KafkaConfigTest {

    @Autowired
    private lateinit var consumerFactory: ConsumerFactory<String, *>

    @Autowired
    private lateinit var kafkaListenerContainerFactory: ConcurrentKafkaListenerContainerFactory<String, *>

    @Test
    fun `should configure consumer factory with correct properties`() {
        val configs = consumerFactory.configurationProperties

        assertThat(configs[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG]).isEqualTo("localhost:9092")
        assertThat(configs[ConsumerConfig.GROUP_ID_CONFIG]).isEqualTo("local-test")
        assertThat(configs[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG]).isEqualTo(StringDeserializer::class.java)
        assertThat(configs[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG]).isEqualTo("latest")
    }

    @Test
    fun `should configure container factory with correct ack mode`() {
        val containerProperties: ContainerProperties = kafkaListenerContainerFactory.containerProperties

        assertThat(containerProperties.ackMode).isEqualTo(ContainerProperties.AckMode.MANUAL_IMMEDIATE)
        assertThat(containerProperties.isSyncCommits).isTrue()
    }
}