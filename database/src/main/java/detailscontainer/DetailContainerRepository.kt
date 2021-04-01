package detailscontainer

import DetailsContainer
import User
import common.DetailMapper
import common.EncryptedDetailEntity
import detail.Detail
import encryption.EncryptedDetail
import encryption.EncryptedDetailsContainer
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import repository.IDetailContainerRepository
import user.UserMapper

open class DetailContainerRepository<T : DetailsContainer>(
    private val userMapper: UserMapper,
    private val detailMapper: DetailMapper,
    private val containerMapper: DetailContainerMapper,
    private val database: DetailContainerDatabase<T>
) : IDetailContainerRepository<T> {

    override fun insertDetail(
        encryptedDetail: EncryptedDetail,
        uuid:String
    ): Completable {
        return database.insertDetail(
            detailMapper.mapToEntity(encryptedDetail),
            uuid
        )
    }

    override fun getEncryptedContainer(uuid:String): Single<EncryptedDetailsContainer> {
        return database.getContainer(uuid)
            .map(containerMapper::mapFromEntity)
            .toSingle()
    }

    override fun getAll(uuid:String): Flowable<List<EncryptedDetail>> {
        return database.getAll(uuid).map {
            detailMapper.mapFromEntities(it)
        }
    }

    override fun deleteDetail(detail: Detail, uuid:String): Completable {
        return database.delete(detail, uuid)
    }

    override fun saveEncryptedContainer(encryptedDetailsContainer: EncryptedDetailsContainer, uuid:String): Completable {
        return database.insertContainer(
            containerMapper.mapToEntity(encryptedDetailsContainer), uuid
        )
    }
}