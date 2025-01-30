package org.coroutines.integrator.domain

// restricted class hierarchies
sealed class ClientEndpoint(val url: String) {
    // singleton instance client1
    object Client1 : ClientEndpoint("http://localhost:5000/client1/")
    object Client2 : ClientEndpoint("http://localhost:5001/client2/")
    object Client3 : ClientEndpoint("http://localhost:5000/client3/")
}