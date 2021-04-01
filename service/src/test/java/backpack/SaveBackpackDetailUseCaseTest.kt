package backpack

import Backpack
import User
import encryption.ContainerType
import encryption.EncryptedDetailsContainer
import encryption.IDetailContainerEncryptionService
import factory.DetailFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import repository.IDetailContainerRepository
import usecases.detailscontainer.SaveDetailsContainerDetailUseCase
import util.EncryptedDetailFactory

class SaveBackpackDetailUseCaseTest {

    private lateinit var saveBackpackTextDetailUseCase: SaveDetailsContainerDetailUseCase<Backpack>
    private val scheduler: Scheduler = mockk()
    private val containerRepository = mockk<IDetailContainerRepository<Backpack>>()
    private val containerEncryptionService =
        mockk<IDetailContainerEncryptionService<Backpack>>()

    private val user: User = mockk(relaxed = true)

    private val detail = DetailFactory.makeRandomDetail()

    @Before
    fun setUp() {
        saveBackpackTextDetailUseCase =
            SaveDetailsContainerDetailUseCase(
                containerRepository,
                containerEncryptionService,
                scheduler,
                Schedulers.trampoline()
            )
    }

    @Test
    fun `when saving a detail to the backpack it is saved to storage`() {
        // Arrange
        val params = SaveDetailsContainerDetailUseCase.Params(user.uuid, user.backpack, detail)

        val encryptedContainer =
            EncryptedDetailsContainer(ContainerType.BACKPACK, "encryptedDEK", "encryptedSDEK")
        every { containerRepository.getEncryptedContainer(user.uuid) } returns Single.just(encryptedContainer)

        val encryptedDetail = EncryptedDetailFactory.makeRandomDetail()
        every {
            containerEncryptionService.encrypt(detail, encryptedContainer)
        } returns Single.just(encryptedDetail)

        every {
            containerRepository.insertDetail(encryptedDetail, user.uuid)
        } returns Completable.complete()

        // Act
        saveBackpackTextDetailUseCase.buildUseCaseObservable(params).test()
            .assertNoErrors()
    }
}