import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import org.junit.Before
import org.junit.Test
import repository.IAuthManager
import usecases.user.LogoutUserUseCase
import java.util.concurrent.Executor

class LogoutUserUseCaseTest {
    private lateinit var logoutUserUseCase: LogoutUserUseCase
    private lateinit var executor: Executor
    private lateinit var scheduler: Scheduler
    private lateinit var authManager: IAuthManager
    private var user= mockk<User>()

    @Before
    fun setUp() {
        executor = mockk()
        scheduler = mockk()
        authManager = mockk(relaxed = true)
        logoutUserUseCase =
            LogoutUserUseCase(
                authManager,
                scheduler
            )
    }

    @Test
    fun LogoutUserUC_Succeeds() {
        // Arrange
        every { authManager.signOut(user.uuid) } returns Completable.complete()

        // Act
        val result = logoutUserUseCase.buildUseCaseObservable(LogoutUserUseCase.Params(user.uuid))

        // Assert
        result.test()
            .assertNoErrors()
    }
}