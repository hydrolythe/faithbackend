import com.google.crypto.tink.KeysetHandle
import encryption.EncryptedAction
import goals.Action
import goals.ActionStatus
import internal.DataEncrypter
import io.reactivex.rxjava3.core.Single

class ActionEncryptionService {

    /**
     * Returns an encrypted version of the [action]
     */
    fun encrypt(action: Action, dek: KeysetHandle): Single<EncryptedAction> {
        val dataEncrypter = DataEncrypter(dek)
        return Single.just(
            EncryptedAction(
                description = dataEncrypter.encrypt(action.description),
                currentStatus = dataEncrypter.encrypt(action.currentStatus.name)
            )
        )
            .doOnSuccess {  }
    }

    /**
     * Decrypts the data of the [encryptedAction], resulting in a regular [Action].
     */
    fun decrypt(
        encryptedAction: EncryptedAction,
        dek: KeysetHandle
    ): Single<Action> {
        val dataEncrypter = DataEncrypter(dek)
        return Single.just(
            Action(
                currentStatus = ActionStatus.valueOf(dataEncrypter.decrypt(encryptedAction.currentStatus))
            )
                .also { it.description = dataEncrypter.decrypt(encryptedAction.description) }
        )
            .doOnSuccess {  }
    }
}
