package be.hogent.faith.faith.models

import com.google.firebase.auth.FirebaseToken

import lombok.AllArgsConstructor
import lombok.Data


@Data
@AllArgsConstructor
class Credentials (
    private val type: CredentialType? = null,
    private val decodedToken: FirebaseToken? = null,
    private val idToken: String? = null,
    private val session: String? = null
)

enum class CredentialType {
    ID_TOKEN, SESSION
}