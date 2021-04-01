import com.google.crypto.tink.KeysetHandle
import detail.*
import encryption.EncryptedDetail
import internal.DataEncrypter
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.io.File
import java.time.LocalDateTime

class DetailEncryptionService(
    private val fileEncryptionService: FileEncryptionService
) {

    /**
     * Returns an encrypted version of the [Detail], both its data and its file.
     */
    fun encrypt(detail: Detail, dek: KeysetHandle, sdek: KeysetHandle): Single<EncryptedDetail> {
        return if (detail is YoutubeVideoDetail) {
            encryptData(detail, dek)
        } else {
            encryptData(detail, dek)
                .zipWith(encryptDetailFiles(detail, sdek)) { encryptedDetail, file ->
                    encryptedDetail.file = file
                    encryptedDetail
                }
        }
    }

    private fun encryptDetailFiles(detail: Detail, sdek: KeysetHandle): Single<File> {
        return fileEncryptionService.encrypt(detail.file, sdek)
            .doOnSuccess {  }
    }

    /**
     * Encrypts the data of  the detail.
     * Does  not set the [EncryptedDetail.file] to an en encrypted version of the file!
     * This should be done afterwards, once the file has been encrypted.
     */
    private fun encryptData(detail: Detail, dek: KeysetHandle): Single<EncryptedDetail> {
        val dataEncrypter = DataEncrypter(dek)
        return Single.just(
            EncryptedDetail(
                file = detail.file,
                uuid = detail.uuid,
                title = dataEncrypter.encrypt(detail.title),
                dateTime = dataEncrypter.encrypt(detail.dateTime.toString()),
                type = dataEncrypter.encrypt(
                    when (detail) {
                        is AudioDetail -> DetailType.Audio
                        is TextDetail -> DetailType.Text
                        is DrawingDetail -> DetailType.Drawing
                        is PhotoDetail -> DetailType.Photo
                        is VideoDetail -> DetailType.ExternalVideo
                        is YoutubeVideoDetail -> DetailType.YoutubeVideo
                        is FilmDetail -> DetailType.Film
                        is ExpandedDetail -> throw IllegalArgumentException("Expanded detail is not unwrapped")
                        else -> throw IllegalArgumentException("Type has not been added")
                    }.toString()
                ),
                thumbnail = detail.thumbnail?.let { dataEncrypter.encrypt(it) },
                youtubeVideoID = when (detail) {
                    is YoutubeVideoDetail -> detail.videoId
                    else -> ""
                }
            )
        )
            .doOnSuccess {  }
    }

    /**
     * Decrypts the data of the [encryptedDetail], resulting in a regular [Detail].
     * The file of the [encryptedDetail] will **not** be decrypted.
     */
    fun decryptData(
        encryptedDetail: EncryptedDetail,
        dek: KeysetHandle
    ): Single<Detail> {
        val dataEncrypter = DataEncrypter(dek)
        val detailTypeString = dataEncrypter.decrypt(encryptedDetail.type)

        return Single.just(
            when (DetailType.valueOf(detailTypeString)) {
                DetailType.Audio -> AudioDetail(
                    file = encryptedDetail.file,
                    title = dataEncrypter.decrypt(encryptedDetail.title),
                    uuid = encryptedDetail.uuid,
                    dateTime = LocalDateTime.parse(dataEncrypter.decrypt(encryptedDetail.dateTime))
                )
                DetailType.Text -> TextDetail(
                    file = encryptedDetail.file,
                    title = dataEncrypter.decrypt(encryptedDetail.title),
                    uuid = encryptedDetail.uuid,
                    dateTime = LocalDateTime.parse(dataEncrypter.decrypt(encryptedDetail.dateTime))
                )
                DetailType.Drawing -> DrawingDetail(
                    file = encryptedDetail.file,
                    title = dataEncrypter.decrypt(encryptedDetail.title),
                    uuid = encryptedDetail.uuid,
                    dateTime = LocalDateTime.parse(dataEncrypter.decrypt(encryptedDetail.dateTime)),
                    thumbnail = encryptedDetail.thumbnail?.let { dataEncrypter.decrypt(it) }
                )
                DetailType.Photo -> PhotoDetail(
                    file = encryptedDetail.file,
                    title = dataEncrypter.decrypt(encryptedDetail.title),
                    uuid = encryptedDetail.uuid,
                    dateTime = LocalDateTime.parse(dataEncrypter.decrypt(encryptedDetail.dateTime)),
                    thumbnail = encryptedDetail.thumbnail?.let { dataEncrypter.decrypt(it) }
                )
                DetailType.YoutubeVideo -> YoutubeVideoDetail(
                    file = encryptedDetail.file,
                    title = dataEncrypter.decrypt(encryptedDetail.title),
                    uuid = encryptedDetail.uuid,
                    dateTime = LocalDateTime.parse(dataEncrypter.decrypt(encryptedDetail.dateTime)),
                    videoId = encryptedDetail.youtubeVideoID
                )
                DetailType.ExternalVideo -> VideoDetail(
                    file = encryptedDetail.file,
                    title = dataEncrypter.decrypt(encryptedDetail.title),
                    uuid = encryptedDetail.uuid,
                    dateTime = LocalDateTime.parse(dataEncrypter.decrypt(encryptedDetail.dateTime)),
                    thumbnail = encryptedDetail.thumbnail?.let { dataEncrypter.decrypt(it) }
                )
                DetailType.Film -> FilmDetail(
                    file = encryptedDetail.file,
                    title = dataEncrypter.decrypt(encryptedDetail.title),
                    uuid = encryptedDetail.uuid,
                    dateTime = LocalDateTime.parse(dataEncrypter.decrypt(encryptedDetail.dateTime)),
                    thumbnail = encryptedDetail.thumbnail?.let { dataEncrypter.decrypt(it) }
                )
            }
        )
            .doOnSuccess {  }
    }

    /**
     * Decrypts the file in a [detail].
     * The file of the detail will be updated to reflect the decrypted file.
     */
    fun decryptDetailFile(
        detail: Detail,
        sdek: KeysetHandle
    ): Completable {
        return Completable.defer {
            // YoutubeVideos don't have a file that needs to be encrypted
            if (detail is YoutubeVideoDetail) {
                Completable.complete()
                    .doOnComplete {  }
            } else {
                val fileEncrypter = FileEncryptionService()
                fileEncrypter.decrypt(detail.file, sdek)
                    .flatMapCompletable {
                        Completable.fromAction {
                            detail.file = it
                        }
                    }
            }
        }
    }

    /**
     * Decrypts the file belonging to a detail, and places it in the given [destinationFile].
     */
    fun decryptDetailFile(
        file: File,
        sdek: KeysetHandle,
        destinationFile: File
    ): Completable {
        return FileEncryptionService().decrypt(file, sdek, destinationFile)
    }

    /**
     * Decrypts the file belonging to a detail, and places it in the given [destinationFile].
     */
    fun decryptDetailFile(
        detail: Detail,
        sdek: KeysetHandle,
        destinationFile: File
    ): Completable {
        return Completable.defer {
            if (detail is YoutubeVideoDetail) {
                Completable
                    .complete()
                    .doOnComplete {  }
            } else {
                decryptDetailFile(detail.file, sdek, destinationFile)
                    .doOnComplete {  }
            }
        }
    }

    /**
     * Decrypts the file belonging to a detail, and places it in the given [destinationFile].
     */
    fun decryptDetailFile(
        encryptedDetail: EncryptedDetail,
        sdek: KeysetHandle,
        destinationFile: File
    ): Completable {
        return Completable.defer {
            if (encryptedDetail.youtubeVideoID.isNotEmpty()) {
                Completable
                    .complete()
                    .doOnComplete {  }
            } else {
                decryptDetailFile(encryptedDetail.file, sdek, destinationFile)
                    .doOnComplete {  }
            }
        }
    }

    internal enum class DetailType {
        Audio,
        Text,
        Drawing,
        Photo,
        ExternalVideo,
        YoutubeVideo,
        Film
    }
}