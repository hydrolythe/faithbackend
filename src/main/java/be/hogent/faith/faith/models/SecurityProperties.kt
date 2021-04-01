package be.hogent.faith.faith.models

import lombok.Data
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component


@Component
@ConfigurationProperties("security")
@Data
class SecurityProperties (
    val cookieProps: CookieProperties? = null,
    val firebaseProps: FirebaseProperties? = null,
    val allowCredentials:Boolean = false,
    val allowedOrigins: List<String>? = null,
    val allowedHeaders: List<String>? = null,
    val exposedHeaders: List<String>? = null,
    val allowedMethods: List<String>? = null,
    val allowedPublicApis: List<String>? = null,
    var superAdmins: List<String>? = null,
    var validApplicationRoles: List<String>? = null
)