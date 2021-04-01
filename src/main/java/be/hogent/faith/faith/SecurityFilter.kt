package be.hogent.faith.faith

import be.hogent.faith.faith.models.CredentialType
import be.hogent.faith.faith.models.Credentials
import be.hogent.faith.faith.models.SecurityProperties
import be.hogent.faith.faith.models.User
import com.google.firebase.auth.FirebaseToken

import org.springframework.security.core.context.SecurityContextHolder

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

import com.google.firebase.auth.FirebaseAuthException

import com.google.firebase.auth.FirebaseAuth

import javax.servlet.http.HttpServletRequest

import javax.servlet.FilterChain

import javax.servlet.http.HttpServletResponse

import org.springframework.beans.factory.annotation.Autowired

import org.springframework.web.filter.OncePerRequestFilter

import lombok.extern.slf4j.Slf4j
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.stereotype.Component
import javax.servlet.http.Cookie



@Component
@Slf4j
internal class SecurityFilter @Autowired constructor(var securityService: SecurityService, var cookieUtils: CookieService, var securityProps: SecurityProperties)
 : OncePerRequestFilter() {

    val ignoredRequests: RequestMatcher = OrRequestMatcher(
        listOf(
            AntPathRequestMatcher("/welcome"),
            AntPathRequestMatcher("/user/**")
        )
    )

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (!ignoredRequests.matches(request)) {
            verifyToken(request)
        }
        filterChain.doFilter(request, response)
    }

    fun verifyToken(request: HttpServletRequest) {
        var session: String? = null
        var decodedToken: FirebaseToken? = null
        var type: CredentialType? = null
        val strictServerSessionEnabled = securityProps.firebaseProps?.enableStrictServerSession ?: false
        val sessionCookie: Cookie? = cookieUtils.getCookie("session")
        val token = securityService.getBearerToken(request)
        try {
            if (sessionCookie != null) {
                session = sessionCookie.value
                decodedToken = FirebaseAuth.getInstance().verifySessionCookie(session)
                type = CredentialType.SESSION
            } else if (!strictServerSessionEnabled) {
                if (token != null && !token.equals("undefined", ignoreCase = true)) {
                    decodedToken = FirebaseAuth.getInstance().verifyIdToken(token)
                    type = CredentialType.ID_TOKEN
                }
            }
        } catch (e: FirebaseAuthException) {
            e.printStackTrace()
        }
        val user: User? = firebaseTokenToUserDto(decodedToken)
        if (user != null) {
            val authentication = UsernamePasswordAuthenticationToken(
                user,
                Credentials(type, decodedToken, token, session), null
            )
            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = authentication
        }
    }

    fun firebaseTokenToUserDto(decodedToken: FirebaseToken?): User? {
        var user: User? = null
        if (decodedToken != null) {
            user = User()
            user.email = decodedToken.email
            user.uid = decodedToken.uid
        }
        return user
    }
}
