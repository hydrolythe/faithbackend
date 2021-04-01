package be.hogent.faith.faith.models

import lombok.Data

@Data
class CookieProperties {
    val domain: String? = null
    val path: String? = null
    val httpOnly = false
    val secure = false
    val maxAgeInMinutes = 0
}