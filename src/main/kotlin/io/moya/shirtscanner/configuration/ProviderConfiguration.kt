package io.moya.shirtscanner.configuration

import io.moya.shirtscanner.services.fetchers.DefaultFetcher
import io.moya.shirtscanner.services.providers.ProductProvider
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ProviderConfiguration(
    private val providerConfigurationProperties: ProviderConfigurationProperties,
) {

    @Bean
    fun fiveBoundlessFetcher() = productProvider(providerConfigurationProperties.fiveBoundless)

    @Bean
    fun grkitsFetcher() = productProvider(providerConfigurationProperties.grkits)

    @Bean
    fun kitsggFetcher() = productProvider(providerConfigurationProperties.kitsgg)

    @Bean
    fun aclotzoneFetcher() = productProvider(providerConfigurationProperties.aclotzone)

    @Bean
    fun fofoshopFetcher() = productProvider(providerConfigurationProperties.fofoshop)

    @Bean
    fun kotofanssFetcher() = productProvider(providerConfigurationProperties.kotofanss)

    @Bean
    fun kegaooFetcher() = productProvider(providerConfigurationProperties.kegaoo)

    @Bean
    fun jjsportFetcher() = productProvider(providerConfigurationProperties.jjsport)

    @Bean
    fun fcstore24Fetcher() = productProvider(providerConfigurationProperties.fcstore24)

    @Bean
    fun jeofc1Fetcher() = productProvider(providerConfigurationProperties.jeofc1)

    @Bean
    fun gkkocFetcher() = productProvider(providerConfigurationProperties.gkkoc)

    @Bean
    fun soccerFetcher() = productProvider(providerConfigurationProperties.soccer)

    private fun productProvider(providerData: ProviderData) = with(providerData) { ProductProvider(DefaultFetcher(url), metadata) }
}


@ConfigurationProperties("providers")
data class ProviderConfigurationProperties(
    @NestedConfigurationProperty
    val fiveBoundless: ProviderData,
    @NestedConfigurationProperty
    val kitsgg: ProviderData,
    @NestedConfigurationProperty
    val grkits: ProviderData,
    @NestedConfigurationProperty
    val aclotzone: ProviderData,
    @NestedConfigurationProperty
    val fofoshop: ProviderData,
    @NestedConfigurationProperty
    val kotofanss: ProviderData,
    @NestedConfigurationProperty
    val kegaoo: ProviderData,
    @NestedConfigurationProperty
    val jjsport: ProviderData,
    @NestedConfigurationProperty
    val fcstore24: ProviderData,
    @NestedConfigurationProperty
    val jeofc1: ProviderData,
    @NestedConfigurationProperty
    val gkkoc: ProviderData,
    @NestedConfigurationProperty
    val soccer: ProviderData,
)

data class ProviderData(
    val url: String,
    @NestedConfigurationProperty
    val metadata: ProviderMetadata,
)

data class ProviderMetadata(
    val name: String,
)
