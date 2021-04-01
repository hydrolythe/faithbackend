package usecases.detail.photodetail

import detail.PhotoDetail
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import thumbnail.ThumbnailProvider
import usecases.base.SingleUseCase
import java.io.File

class CreatePhotoDetailUseCase(
    private val thumbnailProvider: ThumbnailProvider,
    observer: Scheduler
) : SingleUseCase<PhotoDetail, CreatePhotoDetailUseCase.Params>(observer) {

    override fun buildUseCaseSingle(params: Params): Single<PhotoDetail> {
        return Single.fromCallable {
            PhotoDetail(
                file = params.tempPhotoSaveFile,
                thumbnail = thumbnailProvider.getBase64EncodedThumbnail(params.tempPhotoSaveFile)
            )
        }
    }

    class Params(
        val tempPhotoSaveFile: File
    )
}