package be.hogent.faith.faith.models

import lombok.Data

@Data
class FirebaseProperties {
    val sessionExpiryInDays = 0
    val databaseUrl: String? = null
    val enableStrictServerSession = false
    val enableCheckSessionRevoked = false
    val enableLogoutEverywhere = false
}