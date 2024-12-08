package dev.moyis.shirtscanner.infrastructure.controllers

import dev.moyis.shirtscanner.domain.model.Provider
import dev.moyis.shirtscanner.domain.model.ProviderName
import dev.moyis.shirtscanner.domain.model.ProviderStatus
import dev.moyis.shirtscanner.infrastructure.controllers.model.ProviderResponse
import dev.moyis.shirtscanner.testsupport.API_KEY_HEADER
import dev.moyis.shirtscanner.testsupport.AbstractIntegrationTest
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.net.URI

class ProvidersControllerTest : AbstractIntegrationTest() {
    @Test
    fun `providers endpoint returns 200`() {
        Given {
            header(API_KEY_HEADER, apiKey)
        } When {
            get("/v1/providers")
        } Then {
            statusCode(200)
        }
    }

    @Test
    fun `providers endpoint returns all configured providers`() {
        val providers =
            Given {
                header(API_KEY_HEADER, apiKey)
            } When {
                get("/v1/providers")
            } Extract {
                body().jsonPath().getList("", ProviderResponse::class.java)
            }
        assertThat(providers).extracting<String> { it.name }.containsExactlyInAnyOrder("ListR1 Test", "Yupoo Test")
    }

    @Test
    fun `providers endpoint returns status for providers`() {
        tfs.persistProviderData(
            aValidProvider().copy(name = ProviderName("ListR1 Test"), status = ProviderStatus.UP),
            aValidProvider().copy(name = ProviderName("Yupoo Test"), status = ProviderStatus.DOWN),
        )
        val providers =
            Given {
                header(API_KEY_HEADER, apiKey)
            } When {
                get("/v1/providers")
            } Extract {
                body().jsonPath().getList("", ProviderResponse::class.java)
            }
        assertThat(providers)
            .extracting<ProviderStatus> { it.status }
            .containsExactlyInAnyOrder(ProviderStatus.UP, ProviderStatus.DOWN)
    }

    @Test
    fun `returns status UNKNOWN when no status is persisted`() {
        val providers =
            Given {
                header(API_KEY_HEADER, apiKey)
            } When {
                get("/v1/providers")
            } Extract {
                body().jsonPath().getList("", ProviderResponse::class.java)
            }
        assertThat(providers)
            .extracting<ProviderStatus> { it.status }
            .containsExactlyInAnyOrder(ProviderStatus.UNKNOWN, ProviderStatus.UNKNOWN)
    }

    @Test
    fun `checks status when post providers`() {
        Given {
            header(API_KEY_HEADER, apiKey)
        } When {
            post("/v1/providers")
        }

        val providers =
            Given {
                header(API_KEY_HEADER, apiKey)
            } When {
                get("/v1/providers")
            } Extract {
                body().jsonPath().getList("", ProviderResponse::class.java)
            }

        assertThat(providers)
            .extracting<ProviderStatus> { it.status }
            .containsOnly(ProviderStatus.DOWN)
    }
}

private fun aValidProvider() =
    Provider(
        name = ProviderName("ListR1 Test"),
        url = URI("http://test.com"),
        status = ProviderStatus.UP,
    )
