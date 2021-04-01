package usecases.detail.externalvideo

import VideoThumbTaker
import detail.VideoDetail
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import thumbnail.ThumbnailProvider
import usecases.base.SingleUseCase
import java.io.File

class CreateVideoDetailUseCase(
    private val thumbnailProvider: ThumbnailProvider,
    observer: Scheduler
) : SingleUseCase<VideoDetail, CreateVideoDetailUseCase.Params>(observer) {

    override fun buildUseCaseSingle(params: Params): Single<VideoDetail> {
        val vtt = VideoThumbTaker()
        return Single.fromCallable {
            VideoDetail(params.tempSaveFile).apply {
                thumbnail = thumbnailProvider.getBase64EncodedThumbnail(
                        vtt.createVideoThumbnail(
                        file
                    )
                )
            }
        }
    }

    class Params(
        val tempSaveFile: File
    )
}