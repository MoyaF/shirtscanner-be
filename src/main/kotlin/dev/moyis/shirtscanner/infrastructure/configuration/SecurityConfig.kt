package dev.moyis.shirtscanner.infrastructure.configuration

import dev.moyis.shirtscanner.infrastructure.configuration.filters.ApiKeyFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@EnableWebSecurity
@Configuration
class SecurityConfig(
    private val apiKeyFilter: ApiKeyFilter,
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .csrf { it.disable() }
            .addFilterAfter(apiKeyFilter, UsernamePasswordAuthenticationFilter::class.java)
            .authorizeHttpRequests {
                it
                    .requestMatchers("/v1/images/yupoo").permitAll()
                    .requestMatchers("/v1/products").permitAll()
                    .requestMatchers("/v1/products/stream").permitAll()
                    .requestMatchers("/v1/providers").permitAll()
                    .anyRequest().authenticated()
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }.build()
}
