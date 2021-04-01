package usecases.detail.drawingdetail

import detail.DrawingDetail
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import repository.ITemporaryFileStorageRepository
import thumbnail.ThumbnailProvider
import usecases.base.CompletableUseCase
import java.awt.image.BufferedImage

/**
 * Use case to be used when the bitmap inside an existing [DrawingDetail] should be overwritten
 * with a new bitmap. This means the location of the file in the [DrawingDetail] is not changed.
 */
class OverwriteDrawingDetailUseCase(
    private val storageRepo: ITemporaryFileStorageRepository,
    private val thumbnailProvider: ThumbnailProvider,
    observer: Scheduler
) : CompletableUseCase<OverwriteDrawingDetailUseCase.Params>(observer) {

    override fun buildUseCaseObservable(params: Params): Completable {
        return storageRepo.overwriteExistingDrawingDetail(params.bitmap, params.detail)
            .andThen(Completable.fromAction {
                params.detail.thumbnail = thumbnailProvider.getBase64EncodedThumbnail(params.bitmap)
            })
    }

    data class Params(
        val bitmap: BufferedImage,
        val detail: DrawingDetail
    )
}