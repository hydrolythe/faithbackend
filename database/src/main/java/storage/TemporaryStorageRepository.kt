package storage

import Event
import detail.Detail
import detail.DrawingDetail
import detail.TextDetail
import detail.YoutubeVideoDetail
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import repository.ITemporaryFileStorageRepository
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO

class TemporaryStorageRepository(
) : ITemporaryFileStorageRepository {

    /**
     * Stores a detail in its event's folder, and sets the the details path to that location
     */

    override fun storeDetailWithEvent(detail: Detail, event: Event): Completable {
        // As there's no file in a YoutubeVideoDetail, we say complete immediately
        if (detail is YoutubeVideoDetail) {
            return Completable.complete()
        } else {
            return Completable.create {
                event.addDetail(detail)
            }
        }
    }

    override fun storeText(text: String): Single<File> {
        return Single.fromCallable {
            val saveDirectory = File(text, "text")
            saveDirectory.mkdirs()
            val saveFile = File(saveDirectory, UUID.randomUUID().toString())
            saveFile.writeText(text)
            saveFile
        }
    }

    override fun storeBitmap(bitmap: BufferedImage): Single<File> {
        val saveDirectory = File("pictures")
        saveDirectory.mkdirs()
        return storeBitmap(bitmap, saveDirectory, UUID.randomUUID().toString())
    }

    /**
     * Stores the bitmap in the given file
     */
    private fun storeBitmap(bitmap: BufferedImage, file: File): Completable {
        return Completable.fromCallable {
            require(!file.isDirectory) {
                "Must provide a file, not a directory, in which to store the bitmap. ${file.path} is a directory."
            }
            file
        }
    }

    /**
     * Stores the bitmap in the given file in the given folder.
     *
     * @return a Single<File> pointing to the file where the bitmap was saved
     */
    private fun storeBitmap(bitmap: BufferedImage, folder: File, fileName: String): Single<File> {
        require(folder.isDirectory) {
            "Must provide a directory, not a file, in which to store the bitmap.\n" +
                    "${folder.path} is not a directory."
        }
        require(!fileName.isBlank()) { "Empty filenames are not allowed when storing bitmaps" }

        val saveFile = File(folder, "$fileName.png")
        return storeBitmap(bitmap, saveFile).andThen(Single.just(saveFile))
    }

    override fun overwriteExistingDrawingDetail(
        bitmap: BufferedImage,
        drawingDetail: DrawingDetail
    ): Completable {
        return storeBitmap(bitmap, drawingDetail.file)
    }

    override fun loadTextFromExistingDetail(textDetail: TextDetail): Single<String> {
        return Single.fromCallable {
            val readString = textDetail.file.readText()
            readString
        }
    }

    override fun overwriteTextDetail(text: String, existingDetail: TextDetail): Completable {
        return Completable.fromCallable {
            existingDetail.file.writeText(text)
        }
    }

}
