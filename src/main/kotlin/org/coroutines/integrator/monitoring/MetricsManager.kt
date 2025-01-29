package org.coroutines.integrator.monitoring

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class MetricsManager @Autowired constructor(val registry: MeterRegistry) {
    val timer: Timer = registry.timer("service.message.latency.timer")
    val throughputCounter = registry.counter("service.message.throughput.counter")
}