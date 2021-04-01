package encryption

import goals.Goal
import io.reactivex.rxjava3.core.Single

interface IGoalEncryptionService {
    /**
     * Encrypts the [goal]s data.
     *
     * @return an [EncryptedGoal] whose data is encrypted
     */
    fun encrypt(goal: Goal): Single<EncryptedGoal>

    /**
     * Decrypts the [encryptedGoal]s data
     */
    fun decryptData(encryptedGoal: EncryptedGoal): Single<Goal>
}