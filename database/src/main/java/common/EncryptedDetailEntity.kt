package common

import encryption.EncryptedString

data class EncryptedDetailEntity(
    var file: EncryptedString = "",
    var title: EncryptedString = "",
    val uuid: EncryptedString = "",
    val type: EncryptedString = "",
    val dateTime: EncryptedString = "",
    val thumbnail: EncryptedString? = null,
    val youtubeVideoId: EncryptedString = ""
)