package org.coroutines.integrator.monitoring

import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class MetricsManagerTest {

    @Test
    fun `should register metrics correctly`() {
        val registry = SimpleMeterRegistry()

        assertNotNull(registry.find("service.message.latency.timer").timer())
        assertNotNull(registry.find("service.message.throughput.counter").counter())
    }
}