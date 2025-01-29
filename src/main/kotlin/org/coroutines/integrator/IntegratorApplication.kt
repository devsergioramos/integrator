package org.coroutines.integrator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
class IntegratorApplication

fun main(args: Array<String>) {
    runApplication<IntegratorApplication>(*args)
}
