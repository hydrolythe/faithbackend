import detail.Detail
import encryption.ContainerType
import encryption.EncryptedDetail
import encryption.EncryptedDetailsContainer
import encryption.IDetailContainerEncryptionService
import internal.KeyEncrypter
import internal.KeyGenerator
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.Singles

class DetailContainerEncryptionService<T>(
    private val detailEncryptionService: DetailEncryptionService,
    private val keyEncrypter: KeyEncrypter,
    private val keyGenerator: KeyGenerator
) : IDetailContainerEncryptionService<T> {
    override fun encrypt(
        detail: Detail,
        encryptedDetailsContainer: EncryptedDetailsContainer
    ): Single<EncryptedDetail> {
        val dekSingle = keyEncrypter.decrypt(encryptedDetailsContainer.encryptedDEK)
        val sdekSingle = keyEncrypter.decrypt(encryptedDetailsContainer.encryptedStreamingDEK)

        return dekSingle.flatMap { dek ->
            sdekSingle.flatMap { sdek ->
                detailEncryptionService.encrypt(detail, dek, sdek)
                    .doOnSuccess {  }
            }
        }
    }

    override fun decryptFile(detail: Detail, container: EncryptedDetailsContainer): Completable {
        return Completable.create { destinationFile ->
            keyEncrypter.decrypt(container.encryptedStreamingDEK)
                .doOnError {  }
        }
    }

    override fun decryptData(
        encryptedDetail: EncryptedDetail,
        container: EncryptedDetailsContainer
    ): Single<Detail> {
        val dekSingle = keyEncrypter.decrypt(container.encryptedDEK)

        return dekSingle.flatMap { dek ->
            detailEncryptionService.decryptData(encryptedDetail, dek)
        }
    }

    override fun createContainer(type: ContainerType): Single<EncryptedDetailsContainer> {
        val encryptedDEK = keyEncrypter.encrypt(keyGenerator.generateKeysetHandle())
            .doOnSuccess {
                println("Succes 1")
            }
        val encryptedSDEK = keyEncrypter.encrypt(keyGenerator.generateStreamingKeysetHandle())
            .doOnSuccess {
                println("Succes 2")
            }

        return Singles.zip(encryptedDEK, encryptedSDEK) { dek, sdek ->
            EncryptedDetailsContainer(type, dek, sdek)
        }
    }
}