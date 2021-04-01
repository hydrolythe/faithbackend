package detail.audiodetail

import Event
import detail.AudioDetail
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

class SaveEventAudioDetailUseCaseTest {
    private lateinit var saveEventAudioDetailUseCase: SaveEventDetailUseCase
    private val scheduler: Scheduler = mockk()
    private val repository: ITemporaryFileStorageRepository = mockk(relaxed = true)

    private val event = Event()
    private val audioDetail = mockk<AudioDetail>()

    @Before
    fun setUp() {
        saveEventAudioDetailUseCase =
            SaveEventDetailUseCase(
                repository,
                scheduler
            )
    }

    @Test
    fun saveAudioUC_saveAudioNormal_savedToStorage() {
        // Arrange
        every {
            repository.storeDetailWithEvent(
                audioDetail,
                event
            )
        } returns Completable.complete()
        val params = SaveEventDetailUseCase.Params(audioDetail, event)

        // Act
        saveEventAudioDetailUseCase.buildUseCaseObservable(params).test()
            .assertNoErrors()
            .assertComplete()

        // Assert
        verify { repository.storeDetailWithEvent(audioDetail, event) }
    }

    @Test
    fun saveAudioUC_saveAudioNormal_addedToEvent() {
        // Arrange
        every {
            repository.storeDetailWithEvent(
                audioDetail,
                event
            )
        } returns Completable.complete()
        val params = SaveEventDetailUseCase.Params(audioDetail, event)

        // Act
        saveEventAudioDetailUseCase.buildUseCaseObservable(params).test()
            .assertNoErrors()
            .assertComplete()

        // Assert
        assertTrue(event.details.isNotEmpty())

        val eventDetail = event.details.first()
        assertEquals(eventDetail, audioDetail)
    }

    @Test
    fun saveAudioUC_errorInRepo_notAddedToEvent() {
        // Arrange
        every {
            repository.storeDetailWithEvent(
                audioDetail,
                event
            )
        } returns Completable.error(mockk<IOException>())
        val params = SaveEventDetailUseCase.Params(audioDetail, event)

        // Act
        saveEventAudioDetailUseCase.buildUseCaseObservable(params).test()
            .assertError(IOException::class.java)

        // Assert
        assertTrue(event.details.isEmpty())
    }
}