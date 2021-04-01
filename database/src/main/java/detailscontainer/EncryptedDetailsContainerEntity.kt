package detailscontainer

import common.EncryptedDetailEntity
import encryption.EncryptedString

data class EncryptedDetailsContainerEntity(
    val type: String = "",
    val details: List<EncryptedDetailEntity> = emptyList(),
    val encryptedDEK: EncryptedString = "",
    val encryptedStreamingDEK: EncryptedString = ""
)