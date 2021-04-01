package common

import converters.FileConverter
import encryption.EncryptedDetail
import encryption.EncryptedEvent
import event.EncryptedEventEntity
import event.EventMapper
import factory.DataFactory
import factory.EntityFactory
import factory.UserFactory
import junit.framework.Assert.assertEquals
import org.junit.Test

class EventMapperTest {

    private val eventMapper = EventMapper
    private val user = UserFactory.makeUser()

    private val encryptedDetail = EncryptedDetail(
        file = DataFactory.randomFile(),
        title = DataFactory.randomString(),
        uuid = DataFactory.randomUUID(),
        type = "Encrypted type",
        youtubeVideoID = "",
        dateTime = DataFactory.randomString(),
        thumbnail = DataFactory.randomString()
    )
    private val encryptedEventWithDetails = EncryptedEvent(
        dateTime = "encrypted datetime",
        title = "encrypted title",
        emotionAvatar = DataFactory.randomFile(),
        emotionAvatarThumbnail = "encrypted thumbnail",
        notes = "encrypted notes",
        uuid = DataFactory.randomUUID(),
        details = listOf(encryptedDetail),
        encryptedDEK = "encrypted version of DEK",
        encryptedStreamingDEK = "encrypted version of SDEK"
    )
    private val encryptedEventWithoutDetails = EncryptedEvent(
        dateTime = "encrypted datetime",
        title = "encrypted title",
        emotionAvatar = DataFactory.randomFile(),
        emotionAvatarThumbnail = "encrypted thumbnail",
        notes = "encrypted notes",
        uuid = DataFactory.randomUUID(),
        details = emptyList(),
        encryptedDEK = "encrypted version of DEK",
        encryptedStreamingDEK = "encrypted version of SDEK"
    )

    @Test
    fun eventMapper_mapFromEntity_noDetails() {
        val eventEntity = EntityFactory.makeEventEntity()
        val resultingEvent = eventMapper.mapFromEntity(eventEntity)
        assertEqualData(eventEntity, resultingEvent)
    }

    @Test
    fun eventMapper_mapToEntity_noDetails() {
        val resultingEventEntity = eventMapper.mapToEntity(encryptedEventWithoutDetails)
        assertEqualData(resultingEventEntity, encryptedEventWithoutDetails)
    }

    @Test
    fun eventMapper_mapFromEntity_withDetails() {
        val eventEntity = EntityFactory.makeEventEntityWithDetails(5)
        val resultingEvent = eventMapper.mapFromEntity(eventEntity)
        assertEqualData(eventEntity, resultingEvent)
    }

    @Test
    fun eventMapper_mapToEntity_withDetails() {
        val resultingEventEntity = eventMapper.mapToEntity(encryptedEventWithDetails)
        assertEqualData(resultingEventEntity, encryptedEventWithDetails)
    }

    private fun assertEqualData(
        entity: EncryptedEventEntity,
        model: EncryptedEvent
    ) {
        assertEquals(entity.uuid, model.uuid.toString())
        assertEquals(entity.dateTime, model.dateTime)
        assertEquals(entity.title, model.title)
        assertEquals(entity.emotionAvatarThumbnail, model.emotionAvatarThumbnail)
        assertEquals(
            entity.emotionAvatar,
            model.emotionAvatar?.let { FileConverter().toString(it) })
        assertEquals(entity.details.count(), model.details.count())
        entity.details.forEach { detailEntity ->
            val correspondingDetail =
                model.details.find { detailModel -> detailModel.uuid.toString() == detailEntity.uuid }
            assertEqualDetailsData(detailEntity, correspondingDetail!!)
        }
    }

    private fun assertEqualDetailsData(
        entity: EncryptedDetailEntity,
        model: EncryptedDetail
    ) {
        assertEquals(entity.uuid, model.uuid.toString())
        assertEquals(entity.file, FileConverter().toString(model.file))
        assertEquals(entity.type, model.type)
    }
}
