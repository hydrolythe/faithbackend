package network

import detail.YoutubeVideoDetail
import java.io.File

/**
 * Generated from JSON-file to capture the YouTube API results
 * */
data class NetworkVideo(
    val items: List<Item>
)

data class Item(
    val id: Id,
    val snippet: Snippet
)

data class Id(
    val videoId: String
)

data class Snippet(
    val description: String,
    val title: String
)

fun asDomainModel(items: List<Item>): List<YoutubeVideoDetail> {
    return items.map {
        YoutubeVideoDetail(
            file = File(""),
            title = it.snippet.title,
            videoId = it.id.videoId
        )
    }
}
