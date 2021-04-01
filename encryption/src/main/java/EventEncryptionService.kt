import com.google.crypto.tink.KeysetHandle
import detail.Detail
import encryption.EncryptedDetail
import encryption.EncryptedEvent
import encryption.IEventEncryptionService
import internal.DataEncrypter
import internal.KeyEncrypter
import internal.KeyGenerator
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.Singles
import java.time.LocalDateTime

class EventEncryptionService(
    private val detailEncryptionService: DetailEncryptionService,
    private val fileEncryptionService: FileEncryptionService,
    private val keyGenerator: KeyGenerator,
    private val keyEncrypter: KeyEncrypter
) : IEventEncryptionService {

    override fun encrypt(event: Event): Single<EncryptedEvent> {
        val dataKey = keyGenerator.generateKeysetHandle()
        val streamingDataKey = keyGenerator.generateStreamingKeysetHandle()

        val encryptedDetails = encryptDetails(event, dataKey, streamingDataKey)
            .doOnSuccess {  }

        if (event.emotionAvatar != null) {
            val encryptedEmotionAvatar =
                fileEncryptionService
                    .encrypt(event.emotionAvatar!!, streamingDataKey)
                    .doOnSuccess {  }
            return Singles.zip(
                encryptEventData(event, dataKey, streamingDataKey),
                encryptedDetails,
                encryptedEmotionAvatar
            ) { encryptedEvent, details, emotionAvatar ->
                encryptedEvent.details = details
                encryptedEvent.emotionAvatar = emotionAvatar
                encryptedEvent
            }
        } else {
            return encryptEventData(
                event,
                dataKey,
                streamingDataKey
            ).zipWith(encryptedDetails) { encryptedEvent, details ->
                encryptedEvent.details = details
                encryptedEvent
            }
        }
    }

    private fun encryptEventData(
        event: Event,
        dataKey: KeysetHandle,
        streamingKey: KeysetHandle
    ): Single<EncryptedEvent> {
        val encryptedDEK = keyEncrypter.encrypt(dataKey)
            .doOnSuccess {  }
        val encryptedStreamingDEK = keyEncrypter.encrypt(streamingKey)
            .doOnSuccess {  }

        return encryptedDEK.zipWith(encryptedStreamingDEK) { encryptedDek, encryptedSdek ->
            with(DataEncrypter(dataKey)) {
                EncryptedEvent(
                    dateTime = encrypt(event.dateTime.toString()),
                    title = encrypt(event.title!!),
                    emotionAvatar = event.emotionAvatar,
                    emotionAvatarThumbnail = event.emotionAvatarThumbnail?.let { encrypt(it) },
                    notes = event.notes?.let { encrypt(it) },
                    uuid = event.uuid,
                    encryptedDEK = encryptedDek,
                    encryptedStreamingDEK = encryptedSdek
                )
            }
        }
            .doOnSuccess {  }
    }

    private fun encryptDetails(
        event: Event,
        dek: KeysetHandle,
        sdek: KeysetHandle
    ): Single<List<EncryptedDetail>> {
        return Observable
            .fromIterable(event.details)
            .flatMapSingle { detailEncryptionService.encrypt(it, dek, sdek) }
            .toList()
    }

    override fun decryptData(encryptedEvent: EncryptedEvent): Single<Event> {
        return keyEncrypter
            .decrypt(encryptedEvent.encryptedDEK)
            .doOnSuccess {  }
            .flatMap { dek -> decryptEventData(encryptedEvent, dek) }
    }

    private fun decryptEventData(encryptedEvent: EncryptedEvent, dek: KeysetHandle): Single<Event> {
        return decryptDetailsData(dek, encryptedEvent)
            .map { details ->
                with(DataEncrypter(dek)) {
                    Event(
                        dateTime = LocalDateTime.parse(decrypt(encryptedEvent.dateTime)),
                        title = encryptedEvent.title.let { decrypt(it) },
                        emotionAvatar = encryptedEvent.emotionAvatar,
                        emotionAvatarThumbnail = encryptedEvent.emotionAvatarThumbnail?.let { decrypt(it) },
                        notes = encryptedEvent.notes?.let { decrypt(it) },
                        uuid = encryptedEvent.uuid
                    ).apply {
                        details.forEach(::addDetail)
                    }
                }
            }
            .doOnSuccess {  }
    }

    private fun decryptDetailsData(
        dek: KeysetHandle,
        encryptedEvent: EncryptedEvent
    ): Single<List<Detail>> {
        return Observable.fromIterable(encryptedEvent.details)
            .flatMapSingle { detailEncryptionService.decryptData(it, dek) }
            .toList()
            .doOnSuccess {  }
    }

    override fun decryptFiles(encryptedEvent: EncryptedEvent): Completable {
        return keyEncrypter.decrypt(encryptedEvent.encryptedStreamingDEK)
            .doOnSuccess {  }
            .flatMapCompletable {
                Completable.mergeArray(
                    decryptEmotionAvatar(encryptedEvent, it),
                    decryptDetailFiles(encryptedEvent, it)
                )
            }
    }

    private fun decryptEmotionAvatar(
        encryptedEvent: EncryptedEvent,
        sdek: KeysetHandle
    ): Completable {
        return Observable.just(encryptedEvent)
            .flatMapCompletable { event ->
                if (event.emotionAvatar == null) {
                    Completable
                        .complete()
                        .doOnComplete {  }
                } else {
                    Completable.create {
                    fileEncryptionService.decrypt(
                        // Where the file of event.emotionAvatar points to is not relevant, as that is just where it was pointing
                        // the moment it was saved to the database.
                        // The encrypted version of the file should be in localstorage at this time, so we assume that path.
                        event.emotionAvatar!!,
                        sdek)
                    }
                }
            }
    }

    private fun decryptDetailFiles(
        encryptedEvent: EncryptedEvent,
        sdek: KeysetHandle
    ): Completable {
        return Completable
            .merge(
                encryptedEvent.details.map { detail ->
                    // Where the file of the detail points to is not relevant, as that is just where it was pointing
                    // the moment it was saved to the database.
                    // The encrypted version of the file should be in localstorage at this time, so we assume that path.
                    detailEncryptionService.decryptDetailFile(
                        detail,
                        sdek,
                        detail.file
                    )
                }
            )
            .doOnComplete {  }
    }
}
