package common

import Mapper
import encryption.EncryptedDetail
import java.io.File
import java.util.*

object DetailMapper : Mapper<EncryptedDetailEntity, EncryptedDetail> {
    override fun mapToEntity(model: EncryptedDetail): EncryptedDetailEntity {
        return EncryptedDetailEntity(
            file = model.file.path,
            title = model.title,
            uuid = model.uuid.toString(),
            type = model.type,
            dateTime = model.dateTime,
            thumbnail = model.thumbnail,
            youtubeVideoId = model.youtubeVideoID
        )
    }

    override fun mapToEntities(models: List<EncryptedDetail>): List<EncryptedDetailEntity> {
        return models.map(DetailMapper::mapToEntity)
    }

    override fun mapFromEntity(entity: EncryptedDetailEntity): EncryptedDetail {
        return EncryptedDetail(
            file = File(entity.file),
            title = entity.title,
            uuid = UUID.fromString(entity.uuid),
            type = entity.type,
            dateTime = entity.dateTime,
            thumbnail = entity.thumbnail,
            youtubeVideoID = entity.youtubeVideoId
        )
    }

    override fun mapFromEntities(entities: List<EncryptedDetailEntity>): List<EncryptedDetail> {
        return entities.map(DetailMapper::mapFromEntity)
    }
}
