package authentication

import io.reactivex.rxjava3.core.Single
import repository.IAuthManager
import javax.servlet.http.HttpServletRequest

class AuthManager(private val firebase: FirebaseAuthManager) : IAuthManager {

    override fun isUsernameUnique(email: String): Single<Boolean> =
        firebase.isUsernameUnique(email)

    override fun signIn(uuid:String) = firebase.signIn(uuid)

    override fun register(email: String, password: String) = firebase.register(email, password)

    override fun signOut(uuid: String) = firebase.signOut(uuid)

}
