package dev.moyis.shirtscanner.testsupport

import dev.moyis.shirtscanner.Shirtscanner
import dev.moyis.shirtscanner.infrastructure.configuration.properties.ApiKeyFilterConfigurationProperties
import io.restassured.RestAssured
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [Shirtscanner::class],
)
@ContextConfiguration(initializers = [IntegrationTestConfiguration::class])
@ActiveProfiles("test")
@AutoConfigureWebTestClient
abstract class AbstractIntegrationTest {
    @LocalServerPort
    private var port: Int = 0

    @Autowired
    protected lateinit var tfs: TestFixtureService

    @Autowired
    protected lateinit var webTestClient: WebTestClient

    @Autowired
    private lateinit var config: ApiKeyFilterConfigurationProperties

    protected lateinit var apiKey: String

    @BeforeEach
    fun configureEnvironment() {
        RestAssured.port = port
        apiKey = config.apiKey
    }

    @AfterEach
    fun clear() {
        tfs.clearAll()
    }
}

const val API_KEY_HEADER = "X-API-KEY"
