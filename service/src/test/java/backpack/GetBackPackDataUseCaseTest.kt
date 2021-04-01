package backpack

import Backpack
import User
import encryption.ContainerType
import encryption.EncryptedDetailsContainer
import encryption.IDetailContainerEncryptionService
import factory.DetailFactory
import factory.UserFactory
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import repository.IDetailContainerRepository
import usecases.detailscontainer.GetDetailsContainerDataUseCase
import util.EncryptedDetailFactory

class GetBackPackDataUseCaseTest {

    private val containerEncryptionService = mockk<IDetailContainerEncryptionService<Backpack>>()
    private val containerRepository = mockk<IDetailContainerRepository<Backpack>>()
    private lateinit var getBackPackDataUseCase: GetDetailsContainerDataUseCase<Backpack>
    private val user = UserFactory.makeUser()

    @Before
    fun setUp() {
        getBackPackDataUseCase = GetDetailsContainerDataUseCase<Backpack>(
            containerRepository,
            containerEncryptionService,
            observer = mockk(),
            subscriber = Schedulers.trampoline()
        )
    }

    @Test
    fun `returns unencrypted versions of all details in the repo`() {
        // Arrange
        every { containerRepository.getAll(user.uuid) } returns Flowable.just(
            listOf(
                EncryptedDetailFactory.makeRandomDetail(),
                EncryptedDetailFactory.makeRandomDetail()
            )
        )
        every { containerRepository.getEncryptedContainer(user.uuid) } returns Single.just(
            EncryptedDetailsContainer(ContainerType.CINEMA, "", "")
        )
        every {
            containerEncryptionService.decryptData(any(), any())
        } returns Single.defer { Single.just(DetailFactory.makeRandomDetail()) }

        // Act + Assert
        getBackPackDataUseCase.buildUseCaseObservable(GetDetailsContainerDataUseCase.Params(user.uuid))
            .test()
            .assertValue {
                it.size == 2
            }
            .assertComplete()
            .assertNoErrors()
    }
}