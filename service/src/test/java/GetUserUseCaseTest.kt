import encryption.IEventEncryptionService
import factory.DataFactory
import factory.EventFactory
import io.mockk.*
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import repository.IAuthManager
import repository.IEventRepository
import repository.IUserRepository
import usecases.user.GetUserUseCase
import util.EncryptedEventFactory
import java.util.*

class GetUserUseCaseTest {
    private lateinit var getUserUC: GetUserUseCase
    private val eventEncryptionService = mockk<IEventEncryptionService>()
    private val userRepository = mockk<IUserRepository>(relaxed = true)
    private val eventRepository = mockk<IEventRepository>(relaxed = true)
    private val authManager = mockk<IAuthManager>(relaxed = true)

    @Before
    fun setUp() {
        getUserUC = GetUserUseCase(
            userRepository,
            eventRepository,
            eventEncryptionService,
            authManager,
            mockk<Scheduler>(),
            Schedulers.trampoline()
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun getUserUC_execute_callsUserRepo() {
        getUserUC.buildUseCaseObservable(mockk())
        verify { userRepository.get(any()) }
    }

    @Test
    fun getUserUC_execute_callsEventRepo() {
        getUserUC.buildUseCaseObservable(mockk())
        verify { eventRepository.getAll(any()) }
    }

    @Test
    fun getUserUseCase_userPresent_returnsUserWithEvents() {
        val userUuidArg = slot<String>()
        val userUuid = DataFactory.randomUUID().toString()
        val encryptedEvents = EncryptedEventFactory.makeEventList(5)
        val user = User("username", "avatar", UUID.randomUUID().toString())

        every { userRepository.get(capture(userUuidArg)) } returns Flowable.just(user)
        every { eventRepository.getAll(userUuid) } returns Flowable.just(encryptedEvents)
        every { eventEncryptionService.decryptData(any()) } returns Single.defer { // Defer to ensure a new event is made with each call
            Single.just(EventFactory.makeEvent())
        }

        getUserUC.buildUseCaseObservable(mockk())
            .test()
            .assertValue { newUser ->
                newUser.username == "username"
                newUser.events.count() == encryptedEvents.count()
            }
        Assert.assertEquals(userUuid, userUuidArg.captured)
    }
}