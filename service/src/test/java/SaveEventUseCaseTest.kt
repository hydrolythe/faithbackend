import encryption.IEventEncryptionService
import factory.EventFactory
import factory.UserFactory
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import repository.IEventRepository
import thumbnail.ThumbnailProvider
import usecases.event.SaveEventUseCase
import util.EncryptedEventFactory
import java.awt.image.BufferedImage

class SaveEventUseCaseTest {
    private val eventEncryptionService: IEventEncryptionService = mockk(relaxed = true)
    private val eventRepository: IEventRepository = mockk(relaxed = true)
    private val thumbnailProvider: ThumbnailProvider = mockk(relaxed = true)
    private lateinit var saveEventUseCase: SaveEventUseCase

    private lateinit var event: Event
    private lateinit var user: User

    private val encryptedEvent = EncryptedEventFactory.makeEvent()

    @Before
    fun setUp() {
        event = EventFactory.makeEvent(numberOfDetails = 2, hasEmotionAvatar = true)
        user = UserFactory.makeUser(numberOfEvents = 0)
        saveEventUseCase =
            SaveEventUseCase(
                eventEncryptionService,
                eventRepository,
                thumbnailProvider,
                mockk()
            )
        every { eventEncryptionService.encrypt(event) } returns Single.just(encryptedEvent)
        every { eventRepository.insert(encryptedEvent,user.uuid) } returns Completable.complete()
        every { thumbnailProvider.getBase64EncodedThumbnail(any<BufferedImage>()) } returns "b64encThumb"
    }

    @Test
    fun `After saving the event it should be in the user's list of events`() {
        val params = SaveEventUseCase.Params(event, user)

        val result = saveEventUseCase.buildUseCaseObservable(params)

        result.test()
            .dispose()

        assertTrue(user.events.contains(event))
    }

    @Test
    fun `When an error occurs in the EventRepository it returns an Error`() {
        // Arrange
        every { eventRepository.insert(any(),user.uuid) } returns Completable.error(RuntimeException())

        val params = SaveEventUseCase.Params(event, user)

        saveEventUseCase.buildUseCaseObservable(params)
            .test()
            .assertError(RuntimeException::class.java)
    }

    @Test
    fun `When an error occurs in the EventRepository the event is not added to the user's events`() {
        every { eventRepository.insert(any(),user.uuid) } returns Completable.error(RuntimeException())

        val params = SaveEventUseCase.Params(event, user)

        val result = saveEventUseCase.buildUseCaseObservable(params)
        result.test()

        assertFalse(user.events.contains(event))
    }
}