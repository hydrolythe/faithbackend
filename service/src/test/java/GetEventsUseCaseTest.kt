import encryption.IEventEncryptionService
import factory.EventFactory
import factory.UserFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import repository.IEventRepository
import usecases.event.GetEventsUseCase
import util.EncryptedEventFactory

class GetEventsUseCaseTest {
    private lateinit var getEventsUC: GetEventsUseCase
    private val eventEncryptionService = mockk<IEventEncryptionService>()
    private val eventRepository = mockk<IEventRepository>(relaxed=true)
    private val user = UserFactory.makeUser()

    @Before
    fun setUp() {
        getEventsUC = GetEventsUseCase(
            eventRepository,
            eventEncryptionService,
            mockk(),
            Schedulers.trampoline()
        )
    }

    @Test
    fun getEventsUC_execute_callsRepo() {
        every { eventRepository.getAll(user.uuid) } returns Flowable.just(
            EncryptedEventFactory.makeEventList(2),
            EncryptedEventFactory.makeEventList(2)
        )
        val params = GetEventsUseCase.Params(user.uuid)

        getEventsUC.buildUseCaseObservable(params)
            .test()

        verify { eventRepository.getAll(user.uuid) }
    }

    @Test
    fun getEventsUseCase_eventsPresent_returnsThem() {
        val params = GetEventsUseCase.Params(UserFactory.makeUser().uuid)
        // Simulate two events on the stream
        every { eventRepository.getAll(user.uuid) } returns Flowable.just(
            EncryptedEventFactory.makeEventList(2),
            EncryptedEventFactory.makeEventList(2)
        )
        every { eventEncryptionService.decryptData(any()) } returns Single.defer { // Defer to ensure a new event is made with each call
            Single.just(EventFactory.makeEvent())
        }
        getEventsUC.buildUseCaseObservable(params)
            .test()
            .assertValueCount(2)
    }

    @Test
    fun getEventsUseCase_noEventsPresent_returnsNothing() {
        val params = GetEventsUseCase.Params(UserFactory.makeUser().uuid)
        every { eventRepository.getAll(user.uuid) } returns Flowable.empty()
        val result = getEventsUC.buildUseCaseObservable(params)
        result.test().assertNoValues()
    }
}
