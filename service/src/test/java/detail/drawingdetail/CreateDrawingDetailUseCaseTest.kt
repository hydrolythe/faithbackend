package detail.drawingdetail

import factory.DataFactory
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test
import repository.ITemporaryFileStorageRepository
import thumbnail.ThumbnailProvider
import usecases.detail.drawingdetail.CreateDrawingDetailUseCase
import java.awt.image.BufferedImage
import java.io.File
import java.util.concurrent.Executor

class CreateDrawingDetailUseCaseTest {

    private lateinit var createDrawingDetailUseCase: CreateDrawingDetailUseCase
    private lateinit var executor: Executor
    private lateinit var scheduler: Scheduler
    private lateinit var storageRepository: ITemporaryFileStorageRepository
    private val thumbnailProvider = mockk<ThumbnailProvider>()

    private val bitmap = mockk<BufferedImage>()

    @Before
    fun setUp() {
        executor = mockk()
        scheduler = mockk()
        storageRepository = mockk(relaxed = true)
        createDrawingDetailUseCase =
            CreateDrawingDetailUseCase(
                storageRepository,
                thumbnailProvider,
                scheduler
            )
    }

    @Test
    fun createDrawingDetailUC_normal_createsDetailWithCorrectFile() {
        // Arrange
        val saveFile = File("location")
        val thumbnailBitmap = mockk<BufferedImage>()
        val thumbnail = DataFactory.randomString()
        every { storageRepository.storeBitmap(any()) } returns Single.just(saveFile)
        every { thumbnailProvider.getBase64EncodedThumbnail(any<BufferedImage>()) } returns thumbnail

        val params = CreateDrawingDetailUseCase.Params(bitmap)

        // Act
        val result = createDrawingDetailUseCase.buildUseCaseSingle(params)

        // Assert
        result.test()
            .assertNoErrors()
            .assertValue { newDetail ->
                newDetail.file.path == saveFile.path
            }
            .assertValue { newDetail ->
                newDetail.thumbnail == thumbnail
            }
    }
}