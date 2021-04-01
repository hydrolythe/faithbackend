package event

import common.EncryptedDetailEntity
import encryption.EncryptedString
import java.util.*

data class EncryptedEventEntity(
    val dateTime: String = "",
    val title: String = "",
    var emotionAvatar: String? = null,
    var emotionAvatarThumbnail: String? = null,
    val notes: String? = null,
    val uuid: String = UUID.randomUUID().toString(),
    val details: List<EncryptedDetailEntity> = emptyList(),
    val encryptedDEK: EncryptedString = "",
    val encryptedStreamingDEK: EncryptedString = ""
)
