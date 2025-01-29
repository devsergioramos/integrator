package org.coroutines.integrator.kafka

import io.micrometer.core.instrument.Clock
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.coroutines.integrator.entities.PackageStatus
import org.coroutines.integrator.entities.Status
import org.coroutines.integrator.monitoring.MetricsManager
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.kafka.support.Acknowledgment

class PackageStatusConsumerTest {
    private lateinit var meterRegistry: MeterRegistry
    private lateinit var timer: Timer
    private lateinit var counter: Counter
    private lateinit var clock: Clock
    private lateinit var metricsManager: MetricsManager
    private lateinit var consumer: PackageStatusConsumer

    @BeforeEach
    fun setUp() {
        meterRegistry = mock(MeterRegistry::class.java)
        timer = mock(Timer::class.java)
        counter = mock(Counter::class.java)
        clock = mock(Clock::class.java)
        val config = mock(MeterRegistry.Config::class.java)

        `when`(meterRegistry.config()).thenReturn(config)
        `when`(meterRegistry.timer("service.message.latency.timer")).thenReturn(timer)
        `when`(meterRegistry.counter("service.message.throughput.counter")).thenReturn(counter)
        `when`(meterRegistry.config().clock()).thenReturn(clock)

        metricsManager = MetricsManager(meterRegistry)
        consumer = PackageStatusConsumer(metricsManager)
    }

    @Test
    fun `should acknowledge message when processed`() {
        // Given
        val testMessage = PackageStatus(
            1232,
            "New York",
            "London",
            Status.IN_TRANSIT
        )
        val mockAck = mock(Acknowledgment::class.java)

        // When
        consumer.listenPackageStatus(testMessage, 0, mockAck)

        // Then
        verify(mockAck).acknowledge()
    }
}