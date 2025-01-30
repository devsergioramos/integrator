package org.coroutines.integrator.configuration

import okhttp3.MediaType
import okhttp3.OkHttpClient
import org.assertj.core.api.Assertions.assertThat
import org.coroutines.integrator.infra.OkHttpStatusDataSource
import org.coroutines.integrator.types.StatusDataSource
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext

@SpringBootTest
class HttpClientConfigTest {

    @Autowired
    private lateinit var context: ApplicationContext

    @Test
    fun `should create OkHttpClient bean`() {
        val client = context.getBean(OkHttpClient::class.java)
        assertThat(client).isNotNull()
    }

    @Test
    fun `should create JSON media type bean`() {
        val mediaType = context.getBean(MediaType::class.java)
        assertThat(mediaType.toString()).isEqualTo("application/json")
    }

    @Test
    fun `should create StatusDataSource with dependencies`() {
        val dataSource = context.getBean(StatusDataSource::class.java)
        assertThat(dataSource).isInstanceOf(OkHttpStatusDataSource::class.java)

        val okHttpClient = context.getBean(OkHttpClient::class.java)
        val jsonMediaType = context.getBean(MediaType::class.java)
        val impl = dataSource as OkHttpStatusDataSource

        assertThat(impl.client).isSameAs(okHttpClient)
        assertThat(impl.jsonMediaType).isSameAs(jsonMediaType)
    }
}