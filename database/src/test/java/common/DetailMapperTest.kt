package common

import converters.FileConverter
import encryption.EncryptedDetail
import factory.DataFactory
import factory.EntityFactory
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.util.*

class DetailMapperTest {

    private val detailMapper = DetailMapper

    @Test
    fun `should map to EncryptedDetail when EncryptedDetailEntity is given`() {
        val entity = EntityFactory.makeDetailEntity()
        val model = detailMapper.mapFromEntity(entity)
        assertEqualData(entity, model)
    }

    @Test
    fun `should map to DetailEntity when Detail is given`() {
        val model = EncryptedDetail(
            file = DataFactory.randomFile(),
            title = DataFactory.randomString(),
            uuid = UUID.randomUUID(),
            type = "encrypted type",
            youtubeVideoID = "",
            dateTime = DataFactory.randomString(),
            thumbnail = DataFactory.randomString()
        )
        val entity = detailMapper.mapToEntity(model)
        assertEqualData(entity, model)
    }

    private fun assertEqualData(
        entity: EncryptedDetailEntity,
        model: EncryptedDetail
    ) {
        assertEquals(entity.uuid, model.uuid.toString())
        assertEquals(entity.file, FileConverter().toString(model.file))
        assertEquals(entity.type, model.type)
    }
}