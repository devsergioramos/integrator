package org.coroutines.integrator.monitoring

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class MetricsManager @Autowired constructor(val registry: MeterRegistry) {
    val timer: Timer = Timer.builder("service.message.latency.timer")
        .publishPercentileHistogram(true)
        .publishPercentiles(0.5, 0.75, 0.95, 0.99)
        .distributionStatisticExpiry(java.time.Duration.ofMinutes(5))
        .distributionStatisticBufferLength(5)
        .register(registry)

    val throughputCounter = registry.counter("service.message.throughput.counter")
    val errorCounter = registry.counter("service.message.error.counter")
}