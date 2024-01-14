package io.moya.shirtscanner.configuration

import io.moya.shirtscanner.services.cache.CacheService
import io.moya.shirtscanner.services.fetchers.ListR1Fetcher
import io.moya.shirtscanner.services.fetchers.ProductsFetcher
import io.moya.shirtscanner.services.fetchers.SearchKeywordRuFetcher
import io.moya.shirtscanner.services.fetchers.YupooFetcher
import io.moya.shirtscanner.services.providers.ProductProvider
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

private val LOG = KotlinLogging.logger { }

@Configuration
class ProviderConfiguration(
    private val config: FetchersConfigurationProperties,
    private val listR1Fetcher: ListR1Fetcher,
    private val yupooFetcher: YupooFetcher,
    private val searchKeywordRuFetcher: SearchKeywordRuFetcher,
    private val cacheService: CacheService,
) {
    @Bean
    fun providers(): List<ProductProvider> {
        val listR1ProductProviders = getListR1ProductProviders()
        val yupooFetcherProviders = getYupooProductProviders()
        val searchKeywordRuProductProviders = getSearchKeywordRuProductProviders()
        return (listR1ProductProviders + yupooFetcherProviders + searchKeywordRuProductProviders)
            .map { (fetcher, providerData) -> ProductProvider(fetcher, providerData, cacheService) }
    }

    private fun getListR1ProductProviders(): List<Pair<ProductsFetcher, ProviderData>> {
        val fetchersAndProviderData = config.listR1.map { listR1Fetcher to it }
        LOG.info { "Found ${fetchersAndProviderData.size} ListR1 fetchers" }
        return fetchersAndProviderData
    }

    private fun getYupooProductProviders(): List<Pair<ProductsFetcher, ProviderData>> {
        val fetchersAndProviderData = config.yupoo.map { yupooFetcher to it }
        LOG.info { "Found ${fetchersAndProviderData.size} providers using Yupoo fetcher" }
        return fetchersAndProviderData
    }

    private fun getSearchKeywordRuProductProviders(): List<Pair<ProductsFetcher, ProviderData>> {
        val fetchersAndProviderData = config.searchKeywordRu.map { searchKeywordRuFetcher to it }
        LOG.info { "Found ${fetchersAndProviderData.size} providers using Search Keyword RU fetcher" }
        return fetchersAndProviderData
    }
}
