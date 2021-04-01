package detail.drawingdetail

import detail.DrawingDetail
import factory.DataFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import repository.ITemporaryFileStorageRepository
import thumbnail.ThumbnailProvider
import usecases.detail.drawingdetail.OverwriteDrawingDetailUseCase
import java.awt.image.BufferedImage
import java.io.File
import java.util.concurrent.Executor

class OverwriteDrawingDetailUseCaseTest {

    private lateinit var overwriteDrawingDetailUseCase: OverwriteDrawingDetailUseCase
    private lateinit var executor: Executor
    private lateinit var scheduler: Scheduler
    private lateinit var storageRepository: ITemporaryFileStorageRepository

    private val bitmap = mockk<BufferedImage>()
    private val thumbnailProvider = mockk<ThumbnailProvider>()

    @Before
    fun setUp() {
        executor = mockk()
        scheduler = mockk()
        storageRepository = mockk(relaxed = true)
        overwriteDrawingDetailUseCase =
            OverwriteDrawingDetailUseCase(
                storageRepository,
                thumbnailProvider,
                scheduler
            )
        every { thumbnailProvider.getBase64EncodedThumbnail(any<BufferedImage>()) } returns "b64encThumb"
    }

    @Test
    fun overwriteDrawingDetailUC_normal_detailSaveFileStaysSame() {
        // Arrange
        val saveFile = File("location")
        val thumbnail = DataFactory.randomString()
        val drawingDetail = DrawingDetail(saveFile, thumbnail)
        every {
            storageRepository.overwriteExistingDrawingDetail(
                bitmap,
                drawingDetail
            )
        } returns Completable.complete()

        val params = OverwriteDrawingDetailUseCase.Params(bitmap, drawingDetail)

        // Act
        val result = overwriteDrawingDetailUseCase.buildUseCaseObservable(params)

        // Assert
        result.test()
            .assertNoErrors()
        verify { storageRepository.overwriteExistingDrawingDetail(bitmap, drawingDetail) }
        assertEquals(saveFile, drawingDetail.file)
    }
}
