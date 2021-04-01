package encryption

import java.util.*

class EncryptedGoal(
    val uuid: UUID,
    var description: EncryptedString,
    var dateTime: EncryptedString,
    var isCompleted: EncryptedString,
    val currentPositionAvatar: Int,
    val goalColor: EncryptedString,
    val reachGoalWay: EncryptedString = "",
    var subgoals: List<EncryptedSubGoal> = emptyList(),
    /**
     * An encrypted version of the Data Encryption Key, used for envelope encryption.
     * This DEK is used for encrypting small pieces of data directly.
     */
    val encryptedDEK: EncryptedString
)