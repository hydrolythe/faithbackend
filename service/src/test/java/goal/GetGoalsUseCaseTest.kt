package goal

import User
import encryption.IGoalEncryptionService
import factory.GoalFactory
import factory.UserFactory
import io.mockk.*
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import repository.IGoalRepository
import usecases.goal.GetGoalsUseCase
import util.EncryptedGoalFactory

class GetGoalsUseCaseTest {
    private lateinit var getgoalsUC: GetGoalsUseCase
    private val goalEncryptionService = mockk<IGoalEncryptionService>()
    private val goalRepository = mockk<IGoalRepository>(relaxed = true)
    private val user = UserFactory.makeUser()

    @Before
    fun setUp() {
        getgoalsUC =
            GetGoalsUseCase(goalRepository, goalEncryptionService, mockk(), Schedulers.trampoline())
    }

    @Test
    fun `getting goals calls the repository`() {
        every { goalRepository.getAll(user.uuid) } returns Flowable.just(
            EncryptedGoalFactory.makeGoalList(2),
            EncryptedGoalFactory.makeGoalList(2)
        )
        val params = GetGoalsUseCase.Params(user)

        getgoalsUC.buildUseCaseObservable(params)
            .test()

        verify { goalRepository.getAll(user.uuid) }
    }

    @Test
    fun `getting all goals, given goals are present, returns all goals`() {
        val params = GetGoalsUseCase.Params(UserFactory.makeUser())
        // Simulate two list of goals on the stream
        every { goalRepository.getAll(user.uuid) } returns Flowable.just(
            EncryptedGoalFactory.makeGoalList(2),
            EncryptedGoalFactory.makeGoalList(2)
        )
        every { goalEncryptionService.decryptData(any()) } returns Single.defer { // Defer to ensure a new goal is made with each call
            Single.just(GoalFactory.makeGoal())
        }
        getgoalsUC.buildUseCaseObservable(params)
            .test()
            .assertValueCount(2)
    }

    @Test
    fun `getting all goals, no goals present, returns nothing`() {
        val params = GetGoalsUseCase.Params(UserFactory.makeUser())
        every { goalRepository.getAll(user.uuid) } returns Flowable.empty()
        getgoalsUC.buildUseCaseObservable(params).test().assertNoValues()
    }
}