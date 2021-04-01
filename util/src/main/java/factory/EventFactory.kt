package factory

import Event

object EventFactory {

    fun makeEvent(numberOfDetails: Int = 5, hasEmotionAvatar: Boolean = false): Event {
        val event = Event(
            dateTime = DataFactory.randomDateTime(),
            title = DataFactory.randomString(),
            emotionAvatar = if (hasEmotionAvatar) DataFactory.randomFile() else null,
            emotionAvatarThumbnail = if (hasEmotionAvatar) DataFactory.randomString() else null,
            notes = DataFactory.randomString(),
            uuid = DataFactory.randomUUID()
        )
        repeat(numberOfDetails) {
            event.addDetail(DetailFactory.makeRandomDetail())
        }
        return event
    }

    fun makeEventList(count: Int = 5): List<Event> {
        val events = mutableListOf<Event>()
        repeat(count) {
            events.add(makeEvent())
        }
        return events
    }
}