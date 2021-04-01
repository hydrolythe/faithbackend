import detail.PhotoDetail
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import repository.ITemporaryFileStorageRepository
import usecases.event.SaveEventDetailUseCase
import java.io.IOException

class SaveEventPhotoDetailUseCaseTest {
    private val observer = mockk<Scheduler>()
    private val storageRepository = mockk<ITemporaryFileStorageRepository>(relaxed = true)

    private val event = Event()
    private val photoDetail = mockk<PhotoDetail>()
    private lateinit var saveEventPhotoDetailUseCase: SaveEventDetailUseCase

    @Before
    fun setUp() {
        saveEventPhotoDetailUseCase =
            SaveEventDetailUseCase(
                storageRepository,
                observer
            )
    }

    @Test
    fun saveEventPhotoUC_executeNormally_savedToStorage() {
        // Arrange
        every {
            storageRepository.storeDetailWithEvent(photoDetail, event)
        } returns Completable.complete()
        val params = SaveEventDetailUseCase.Params(photoDetail, event)

        // Act
        saveEventPhotoDetailUseCase.buildUseCaseObservable(params).test()
            .assertNoErrors()
            .assertComplete()

        // Assert
        verify { storageRepository.storeDetailWithEvent(photoDetail, event) }
    }

    @Test
    fun saveEventPhotoUC_executeNormally_addedToEvent() {
        // Arrange
        every {
            storageRepository.storeDetailWithEvent(photoDetail, event)
        } returns Completable.complete()
        val params = SaveEventDetailUseCase.Params(photoDetail, event)

        // Act
        val result = saveEventPhotoDetailUseCase.buildUseCaseObservable(params)
        result.test()

        // Assert
        assertTrue(event.details.isNotEmpty())

        val eventDetail = event.details.first()
        assertEquals(eventDetail, photoDetail)
    }

    @Test
    fun saveEventPhotoUC_errorInRepo_notAddedToEvent() {
        // Arrange
        every {
            storageRepository.storeDetailWithEvent(photoDetail, event)
        } returns Completable.error(mockk<IOException>())
        val params = SaveEventDetailUseCase.Params(photoDetail, event)

        // Act
        val result = saveEventPhotoDetailUseCase.buildUseCaseObservable(params)
        result.test()
            .assertError(IOException::class.java)

        // Assert
        assertTrue(event.details.isEmpty())
    }
}