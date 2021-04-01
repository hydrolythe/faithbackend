package be.hogent.faith.faith

import be.hogent.faith.faith.models.SecurityProperties
import org.springframework.security.config.http.SessionCreationPolicy

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

import org.springframework.security.config.annotation.web.builders.HttpSecurity

import org.springframework.web.cors.CorsConfiguration

import org.springframework.security.web.AuthenticationEntryPoint

import org.springframework.beans.factory.annotation.Autowired

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.lang.Exception
import java.sql.Timestamp
import java.util.*
import org.springframework.security.web.util.matcher.OrRequestMatcher

import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher

import java.util.LinkedList





@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
class SecurityConfig : WebSecurityConfigurerAdapter() {
    @Autowired
    private val objectMapper: ObjectMapper? = null

    @Autowired
    private val restSecProps: SecurityProperties? = null

    @Autowired
    private val tokenAuthenticationFilter: SecurityFilter? = null
    @Bean
    fun restAuthenticationEntryPoint(): AuthenticationEntryPoint {
        return AuthenticationEntryPoint { httpServletRequest, httpServletResponse, e ->
            val errorObject: MutableMap<String, Any> = HashMap()
            val errorCode = 401
            errorObject["message"] = "Unauthorized access of protected resource, invalid credentials"
            errorObject["error"] = HttpStatus.UNAUTHORIZED
            errorObject["code"] = errorCode
            errorObject["timestamp"] = Timestamp(Date().getTime())
            httpServletResponse.contentType = "application/json;charset=UTF-8"
            httpServletResponse.status = errorCode
            httpServletResponse.writer.write(objectMapper!!.writeValueAsString(errorObject))
        }
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = restSecProps?.allowedOrigins
        configuration.allowedMethods = restSecProps?.allowedMethods
        configuration.allowedHeaders = restSecProps?.allowedHeaders
        configuration.allowCredentials = restSecProps?.allowCredentials
        configuration.exposedHeaders = restSecProps?.exposedHeaders
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    private var SECURITY_EXCLUSION_MATCHER: RequestMatcher? = OrRequestMatcher(listOf(AntPathRequestMatcher("/welcome"),AntPathRequestMatcher("/user/**")))


    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.cors().configurationSource(corsConfigurationSource()).and().csrf().disable().formLogin().disable()
            .httpBasic().disable().exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint())
            .and().authorizeRequests().requestMatchers(SECURITY_EXCLUSION_MATCHER).permitAll()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll().anyRequest().authenticated().and()
            .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }
}