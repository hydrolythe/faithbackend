package backpack

import Backpack
import User
import detail.AudioDetail
import factory.UserFactory
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import junit.framework.Assert.assertFalse
import org.junit.After
import org.junit.Before
import org.junit.Test
import repository.IDetailContainerRepository
import usecases.detailscontainer.DeleteDetailsContainerDetailUseCase

class DeleteBackpackDetailUseCaseTest {
    private lateinit var deleteBackpackDetailUseCase: DeleteDetailsContainerDetailUseCase<Backpack>
    private val detailContainerRepo: IDetailContainerRepository<Backpack> = mockk(relaxed = true)

    private val detail = mockk<AudioDetail>()
    private val user = UserFactory.makeUser()
    private lateinit var backpack: Backpack

    @Before
    fun setUp() {
        backpack = Backpack()
        backpack.addDetail(detail)

        every { detailContainerRepo.deleteDetail(detail,user.uuid) } returns Completable.complete()

        deleteBackpackDetailUseCase =
            DeleteDetailsContainerDetailUseCase(
                detailContainerRepo,
                mockk()
            )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `deleting a detail from a container removes it from the database`() {
        // Arrange
        val params = DeleteDetailsContainerDetailUseCase.Params(user.uuid,detail, backpack)

        // Act
        deleteBackpackDetailUseCase.buildUseCaseObservable(params)
            .test()
            .assertNoErrors()
            .assertComplete()

        // Assert
        verify { detailContainerRepo.deleteDetail(detail,user.uuid) }
    }

    @Test
    fun `deleting a detail from a container removes it from the container`() {
        // Arrange
        val params = DeleteDetailsContainerDetailUseCase.Params(user.uuid,detail, backpack)

        // Act
        deleteBackpackDetailUseCase.buildUseCaseObservable(params)
            .test()
            .assertNoErrors()
            .assertComplete()

        // Assert
        assertFalse(backpack.details.contains(detail))
    }
}