package io.moya.shirtscanner.services.fetchers

import io.moya.shirtscanner.models.Product
import io.moya.shirtscanner.models.SearchResult
import io.moya.shirtscanner.services.WebConnector
import org.jsoup.nodes.Element
import org.springframework.stereotype.Service

@Service
class SearchKeywordRuFetcher(
    private val webConnector: WebConnector,
) : ProductsFetcher {
    override fun search(q: String, url: String): SearchResult {
        val products = getProducts(q, url)
        return SearchResult(
            queryUrl = getQueryUrl(q, url),
            products = products,
        )
    }

    private fun getProducts(
        q: String,
        url: String,
    ): List<Product> {
        val query = getQueryUrl(q, url)
        val document = webConnector.fetchDocument(query) ?: return listOf()
        return document.getElementsByClass("prod_box")
            .asSequence()
            .mapNotNull { mapToProduct(it, url) }
            .toList()
    }

    private fun mapToProduct(
        element: Element,
        baseUrl: String,
    ): Product? {
        val a = element.selectFirst("a") ?: return null
        val price = element.getElementsByClass("price").first() ?: return null
        val imageLink = element.selectFirst("img")?.attr("src") ?: return null
        val productLink = a.attr("href")
        val name = a.attr("title")
        return Product(
            name = name,
            price = price.text(),
            productLink = "$baseUrl$productLink",
            imageLink = "$$baseUrl$imageLink",
        )
    }

    private fun getQueryUrl(
        q: String,
        url: String,
    ) = """$url/search/?Keyword=$q"""
}