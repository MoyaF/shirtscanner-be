package dev.moyis.shirtscanner.domain

import dev.moyis.shirtscanner.domain.api.ProductService
import dev.moyis.shirtscanner.domain.model.ProviderName
import dev.moyis.shirtscanner.domain.model.ProviderStatus
import dev.moyis.shirtscanner.domain.model.SearchResult
import dev.moyis.shirtscanner.domain.spi.ProductProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import reactor.test.StepVerifier
import java.net.URI

class ProductServiceTest {
    @Nested
    inner class ABlockingSearch {
        @Test
        fun `returns empty with no configured providers`() {
            val productService = ProductService(emptyList())

            val results = productService.search("any")

            assertThat(results).isEmpty()
        }

        @Test
        fun `returns a result for every configured provider`() {
            val productService = ProductService(listOf(FakeProvider, FakeProvider, FakeProvider, FakeProvider))

            val results = productService.search("any")

            assertThat(results).hasSize(4)
        }
    }

    @Nested
    inner class AReactiveSearch {
        @Test
        fun `returns empty with no configured providers`() {
            val productService = ProductService(emptyList())

            StepVerifier.create(productService.searchStream("any"))
                .expectNextCount(0)
                .expectComplete()
                .verify()
        }

        @Test
        fun `returns a result for every configured provider`() {
            val productService = ProductService(listOf(FakeProvider, FakeProvider, FakeProvider, FakeProvider))

            StepVerifier.create(productService.searchStream("any"))
                .expectNextCount(4)
                .expectComplete()
                .verify()
        }
    }
}

private object FakeProvider : ProductProvider {
    override val url = URI("https://example.com/search")
    override val name = ProviderName("FixedProvider")

    override fun search(query: String) =
        SearchResult(
            providerName = "FixedProvider",
            queryUrl = URI("https://example.com/search?q=$query"),
            products = emptyList(),
        )

    override fun status() = ProviderStatus.UP
}
