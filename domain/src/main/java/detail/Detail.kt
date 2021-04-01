package detail

import java.io.File
import java.io.Serializable
import java.time.LocalDateTime
import java.util.UUID

/**
 * A detail that can be part of an event, solution,...
 */
open class Detail(
    /**
     * A relative path of where the actual content of the detail is saved.
     * Relative because when getting it from local storage the necessary directory structure is
     * added before the path.
     */
    var file: File,
    var title: String = "",
    val uuid: UUID = UUID.randomUUID(),
    var dateTime: LocalDateTime = LocalDateTime.now(),
    // contains a string : base64 encoded thumbnail of the image.
    var thumbnail: String? = null
) : Serializable

enum class DetailType{
    DRAWING,PHOTO,TEXT,AUDIO,VIDEO,YOUTUBE
}

class ExpandedDetail(
    file: File,
    title: String = "",
    uuid: UUID = UUID.randomUUID(),
    dateTime: LocalDateTime = LocalDateTime.now(),
    thumbnail: String?,
    var detailType:DetailType
):Detail(file,title,uuid,dateTime,thumbnail)

class DrawingDetail(
    file: File,
    thumbnail: String?,
    title: String = "",
    uuid: UUID = UUID.randomUUID(),
    dateTime: LocalDateTime = LocalDateTime.now()
) : Detail(file, title, uuid, dateTime, thumbnail)

class PhotoDetail(
    file: File,
    thumbnail: String?,
    title: String = "",
    uuid: UUID = UUID.randomUUID(),
    dateTime: LocalDateTime = LocalDateTime.now()
) : Detail(file, title, uuid, dateTime, thumbnail)

class TextDetail(
    file: File,
    title: String = "",
    uuid: UUID = UUID.randomUUID(),
    dateTime: LocalDateTime = LocalDateTime.now()
) : Detail(file, title, uuid, dateTime)

class AudioDetail(
    file: File,
    title: String = "",
    uuid: UUID = UUID.randomUUID(),
    dateTime: LocalDateTime = LocalDateTime.now()
) : Detail(file, title, uuid, dateTime)

class VideoDetail(
    file: File,
    title: String = "",
    uuid: UUID = UUID.randomUUID(),
    dateTime: LocalDateTime = LocalDateTime.now(),
    thumbnail: String? = null
) : Detail(file, title, uuid, dateTime, thumbnail)

class YoutubeVideoDetail(
    file: File = File("YoutubeVideoDetail/has/no/file"),
    title: String = "",
    uuid: UUID = UUID.randomUUID(),
    val videoId: String,
    dateTime: LocalDateTime = LocalDateTime.now()
) : Detail(file, title, uuid, dateTime)

/**
 * Represenents films that have been made using the Cinema functionality.
 */
class FilmDetail(
    file: File,
    title: String = "",
    uuid: UUID = UUID.randomUUID(),
    dateTime: LocalDateTime = LocalDateTime.now(),
    thumbnail: String?
) : Detail(file, title, uuid, dateTime, thumbnail)
