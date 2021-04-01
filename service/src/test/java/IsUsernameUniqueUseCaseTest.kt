import com.google.common.util.concurrent.AbstractScheduledService
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import repository.IAuthManager
import usecases.user.IsUsernameUniqueUseCase
import java.util.concurrent.Executor

class IsUsernameUniqueUseCaseTest {
    private lateinit var isUsernameUniqueUserUseCase: IsUsernameUniqueUseCase
    private lateinit var executor: Executor
    private lateinit var scheduler: Scheduler
    private lateinit var authManager: IAuthManager

    @Before
    fun setUp() {
        executor = mockk()
        scheduler = mockk()
        authManager = mockk(relaxed = true)
        isUsernameUniqueUserUseCase =
            IsUsernameUniqueUseCase(
                authManager,
                scheduler
            )
    }

    @Test
    fun isUsernameUniqueUC_existingUser_ReturnsFalse() {
        val usernameArg = slot<String>()
        every {
            authManager.isUsernameUnique(
                capture(usernameArg)
            )
        } returns Single.just(false)

        val params = IsUsernameUniqueUseCase.Params("an")

        val result = isUsernameUniqueUserUseCase.buildUseCaseSingle(params)

        result.test()
            .assertNoErrors()
            .assertValue { it == false }

        Assert.assertEquals(params.username + "@faith.be", usernameArg.captured)
    }

    @Test
    fun isUsernameUniqueUC_nonExistingUser_ReturnsTrue() {
        val usernameArg = slot<String>()
        every {
            authManager.isUsernameUnique(
                capture(usernameArg)
            )
        } returns Single.just(true)

        val params = IsUsernameUniqueUseCase.Params("an")

        val result = isUsernameUniqueUserUseCase.buildUseCaseSingle(params)

        result.test()
            .assertNoErrors()
            .assertValue { it == true }

        Assert.assertEquals(params.username + "@faith.be", usernameArg.captured)
    }
}