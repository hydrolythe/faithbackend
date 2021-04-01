package be.hogent.faith.faith

import be.hogent.faith.faith.models.Credentials
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.security.core.context.SecurityContext
import javax.servlet.http.HttpServletRequest

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils


@Service
class SecurityService {
    val user: SecurityProperties.User?
        get() {
            var userPrincipal: SecurityProperties.User? = null
            val securityContext: SecurityContext = SecurityContextHolder.getContext()
            val principal: Any = securityContext.getAuthentication().getPrincipal()
            if (principal is SecurityProperties.User) {
                userPrincipal = principal
            }
            return userPrincipal
        }
    val credentials: Credentials
        get() {
            val securityContext: SecurityContext = SecurityContextHolder.getContext()
            return securityContext.getAuthentication().getCredentials() as Credentials
        }

    fun getBearerToken(request: HttpServletRequest): String? {
        var bearerToken: String? = null
        val authorization = request.getHeader("Authorization")
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            bearerToken = authorization.substring(7, authorization.length)
        }
        return bearerToken
    }
}