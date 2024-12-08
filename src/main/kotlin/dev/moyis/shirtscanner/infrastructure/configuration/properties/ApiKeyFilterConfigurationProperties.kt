package dev.moyis.shirtscanner.infrastructure.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "security")
data class ApiKeyFilterConfigurationProperties(
    val enabled: Boolean,
    val apiKey: String,
)
