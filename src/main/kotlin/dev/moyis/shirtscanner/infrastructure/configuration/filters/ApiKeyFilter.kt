package dev.moyis.shirtscanner.infrastructure.configuration.filters

import dev.moyis.shirtscanner.infrastructure.configuration.properties.ApiKeyFilterConfigurationProperties
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

private const val API_KEY_HEADER = "X-API-KEY"

@Component
class ApiKeyFilter(
    config: ApiKeyFilterConfigurationProperties,
) : OncePerRequestFilter() {
    private val enabled = config.enabled
    private val secretApiKey = config.apiKey

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val requestApiKey = request.getHeader(API_KEY_HEADER)
        if (!enabled || secretApiKey == requestApiKey) {
            filterChain.doFilter(request, response)
        } else {
            with(response) {
                status = HttpServletResponse.SC_UNAUTHORIZED
                writer.write("Unauthorized")
            }
        }
    }
}
