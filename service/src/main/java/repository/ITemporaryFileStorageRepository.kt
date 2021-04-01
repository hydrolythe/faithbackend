package repository

import Event
import detail.Detail
import detail.DrawingDetail
import detail.TextDetail
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.awt.image.BufferedImage
import java.io.File

/**
 * The temporary storage is to be used when constructing something (eg. an Event), but the user
 * is not finished yet. As such these files should be saved in the cache.
 * Once the user is finished (and saves the event), these files can be moved to permanent storage
 * using an [IFileStorageRepository].
 */
interface ITemporaryFileStorageRepository {

    /**
     * Stores text the device's cache directory
     */
    fun storeText(text: String): Single<File>

    /**
     * Stores a bitmap in the device's cache directory
     */
    fun storeBitmap(bitmap: BufferedImage): Single<File>

    fun overwriteExistingDrawingDetail(bitmap: BufferedImage, drawingDetail: DrawingDetail): Completable
    fun overwriteTextDetail(text: String, existingDetail: TextDetail): Completable

    fun storeDetailWithEvent(detail: Detail, event: Event): Completable

    fun loadTextFromExistingDetail(textDetail: TextDetail): Single<String>
}