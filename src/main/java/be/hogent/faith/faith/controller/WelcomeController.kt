package be.hogent.faith.faith.controller

import be.hogent.faith.faith.CookieService
import be.hogent.faith.faith.SecurityService
import be.hogent.faith.faith.iservice.IWelcomeService
import be.hogent.faith.faith.models.SecurityProperties
import controllerModels.Token
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.EntityModel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest




@RestController
@RequestMapping("/welcome")
class WelcomeController @Autowired constructor(val securityService: SecurityService, val welcomeService: IWelcomeService, val cookieUtils: CookieService, val secProps: SecurityProperties) {
    @GetMapping
    fun loginUser(httpServletRequest: HttpServletRequest): Token {
        val idToken: String? = securityService.getBearerToken(httpServletRequest)
        val token = idToken?.let { welcomeService.login(it) }
        val sessionExpiryDays: Int = secProps.firebaseProps?.sessionExpiryInDays ?: 7
        cookieUtils.setSecureCookie("session", token, sessionExpiryDays)
        cookieUtils.setCookie("authenticated", true.toString(),sessionExpiryDays)
        return Token(token)
    }
}