package authentication

import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserRecord
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import repository.*

import io.reactivex.rxjava3.core.MaybeOnSubscribe
import org.springframework.util.StringUtils
import javax.servlet.http.HttpServletRequest
import com.google.firebase.auth.FirebaseAuthException

import com.google.firebase.auth.SessionCookieOptions
import java.util.concurrent.TimeUnit
import javax.servlet.http.Cookie


class FirebaseAuthManager(
    private val auth: FirebaseAuth
) {

    /**
     * check if email is unique when user wants to register
     */
    fun isUsernameUnique(email: String): Single<Boolean> {
        return Maybe.create(MaybeOnSubscribe<Any?> { emitter ->
            emitter.onSuccess(auth.getUserByEmail(email))
        })
            .map { it.toString().isEmpty() }
            .onErrorResumeNext { error: Throwable ->
                when (error) {
                    is FirebaseException -> Maybe.error(NetworkError(error))
                    else -> Maybe.error(
                        RuntimeException(
                            "Error fetching providers for email",
                            error
                        )
                    )
                }
            }
            .toSingle()
    }

    /**
     * register the user
     */
    fun register(email: String, password: String): Maybe<String?> {
        return Maybe.create(MaybeOnSubscribe<Any?> { emitter ->
            val cr: UserRecord.CreateRequest = UserRecord.CreateRequest().setEmail(email).setPassword(password)
            emitter.onSuccess(auth.createUser(cr))
        }).map { mapToUserUID(it) }
            // map FirebaseExceptions to domain exceptions
            .onErrorResumeNext { error: Throwable ->
                when (error) {
                    is FirebaseException -> Maybe.error(NetworkError(error))
                    else -> Maybe.error(Throwable(error))
                }
            }
    }

    /**
     * sign in the user
     */
    fun signIn(uuid:String): Maybe<String?> {
        return Maybe.create(MaybeOnSubscribe<String?> {
            val sessionExpiryDays: Int = 7
            val expiresIn = TimeUnit.DAYS.toMillis(sessionExpiryDays.toLong())
            val options = SessionCookieOptions.builder().setExpiresIn(expiresIn).build()
            it.onSuccess(auth.createSessionCookie(uuid,options))
        }).onErrorResumeNext { error: Throwable ->
            when (error) {
                is FirebaseException -> Maybe.error(NetworkError(error))
                else -> Maybe.error(SignInException(error))
            }
        }
    }

    fun signOut(uuid: String): Completable {
        return try {
            auth.revokeRefreshTokens(uuid)
            Completable.complete()
        } catch (e: Exception) {
            Completable.error(SignOutException(e))
        }
    }

    private fun mapToUserUID(result: Any?): String? {
        return result.toString()
    }
}