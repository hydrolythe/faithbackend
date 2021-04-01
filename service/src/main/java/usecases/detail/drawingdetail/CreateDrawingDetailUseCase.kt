package usecases.detail.drawingdetail

import detail.DrawingDetail
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import repository.ITemporaryFileStorageRepository
import thumbnail.ThumbnailProvider
import usecases.base.SingleUseCase
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.imageio.ImageIO

class CreateDrawingDetailUseCase(
    private val storageRepository: ITemporaryFileStorageRepository,
    private val thumbnailProvider: ThumbnailProvider,
    observer: Scheduler
) : SingleUseCase<DrawingDetail, CreateDrawingDetailUseCase.Params>(observer) {

    override fun buildUseCaseSingle(params: Params): Single<DrawingDetail> {
        return storageRepository.storeBitmap(params.bitmap).map { storedFile ->
            val name = storedFile.name
            val outputStream = FileOutputStream(storedFile)
            ImageIO.write(params.bitmap,"png",outputStream)
            DrawingDetail(
                file = storedFile,
                thumbnail = thumbnailProvider.getBase64EncodedThumbnail(params.bitmap)
            )
        }
    }

    class Params(
        val bitmap: BufferedImage
    )
}