import detail.AudioDetail
import detail.Detail
import detail.TextDetail
import java.io.File
import java.time.LocalDateTime
import java.util.UUID

data class Event(
    /**
     * The time when this event occured.
     */
    var dateTime: LocalDateTime = LocalDateTime.now(),

    /**
     * The title doesn't have to be filled in immediately when recording it, but it _has_ to be filled in once the
     * mentor fills in the overview.
     */
    var title: String? = null,
    /**
     * The file where the colored avatar is saved.
     * It is first saved when the user draws something, before that the property is null.
     */
    var emotionAvatar: File? = null,
    var emotionAvatarThumbnail: String? = null,
    /**
     * The notes that were added to this event.
     */
    var notes: String? = null,

    val uuid: UUID = UUID.randomUUID()
) {
    private val _details = mutableListOf<Detail>()
    val details: List<Detail>
        get() = _details

    fun addDetail(detail: Detail) {
        _details += detail
    }

    fun removeDetail(detail: Detail) {
        _details -= detail
    }

    fun addNewAudioDetail(saveFile: File) {
        addDetail(AudioDetail(saveFile))
    }

    fun addNewTextDetail(saveFile: File) {
        addDetail(TextDetail(saveFile))
    }
}