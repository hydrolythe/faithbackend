package goal

import factory.UserFactory
import goals.Goal
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import org.junit.Before
import org.junit.Test
import repository.IGoalRepository
import usecases.goal.DeleteGoalUseCase

class DeleteGoalUseCaseTest {
    private val goalRepository = mockk<IGoalRepository>()
    private lateinit var deleteGoalUC: DeleteGoalUseCase
    private val goal = mockk<Goal>()
    private val user = UserFactory.makeUser()

    @Before
    fun setUp() {
        deleteGoalUC = DeleteGoalUseCase(goalRepository, mockk())
    }

    @Test
    fun `deleting a goal returns Completable on success`() {
        // Arrange
        val params = DeleteGoalUseCase.Params(goal, user.uuid)
        every { goalRepository.delete(goal.uuid,user.uuid) } returns Completable.complete()

        // Act
        deleteGoalUC.buildUseCaseObservable(params)
            .test()
            .assertNoErrors()
            .assertComplete()

        // Assert
        verify { goalRepository.delete(goal.uuid,user.uuid) }
    }

    @Test
    fun `deleting a goal removes it from the user`() {
        // Arrange
        // TODO user.addGoal(goal)
        val params = DeleteGoalUseCase.Params(goal, user.uuid)
        every { goalRepository.delete(goal.uuid,user.uuid) } returns Completable.complete()

        // Act
        deleteGoalUC.buildUseCaseObservable(params)
            .test()
            .dispose()

        // Assert
        // TODO   Assert.assertFalse(user.goals.contains(goal))
    }

    @Test
    fun `deleting a goal when an error occurs in the goalRepository returns an error`() {
        val params = DeleteGoalUseCase.Params(goal, user.uuid)
        every { goalRepository.delete(goal.uuid,user.uuid) } returns Completable.error(RuntimeException())

        // Act
        deleteGoalUC.buildUseCaseObservable(params)
            .test()
            .assertError(RuntimeException::class.java)
    }
}
