package org.coroutines.integrator.types

import com.fasterxml.jackson.annotation.JsonProperty

enum class Status {
    @JsonProperty("pending") PENDING,
    @JsonProperty("in_transit") IN_TRANSIT,
    @JsonProperty("delivered") DELIVERED,
    @JsonProperty("cancelled") CANCELLED
}


data class PackageStatus(
    @JsonProperty("id") val packageId: Int,
    @JsonProperty("actual_address") val actualAddress: String,
    @JsonProperty("next_address") val nextAddress: String,
    @JsonProperty("status") val status: Status
)