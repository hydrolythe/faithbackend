package detail.textdetail

import detail.TextDetail
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test
import repository.ITemporaryFileStorageRepository
import usecases.detail.textdetail.LoadTextDetailUseCase

class LoadTextDetailUseCaseTest {
    private lateinit var loadTextUseCase: LoadTextDetailUseCase
    private val scheduler: Scheduler = mockk()
    private val repository: ITemporaryFileStorageRepository = mockk(relaxed = true)

    private val text = "<font size='5'>Hello <b>World</b></font>"
    private val existingDetail = mockk<TextDetail>()

    @Before
    fun setUp() {
        loadTextUseCase =
            LoadTextDetailUseCase(repository, scheduler)
    }

    @Test
    fun loadTextUC_normal_returnsTextFromFile() {
        // Arrange
        every { repository.loadTextFromExistingDetail(existingDetail) } returns Single.just(text)

        // Act
        loadTextUseCase.buildUseCaseSingle(
            LoadTextDetailUseCase.LoadTextParams(existingDetail)
        ).test()
            .assertValue(text)

        // Assert
        verify { repository.loadTextFromExistingDetail(existingDetail) }
    }

    @Test
    fun loadTextUC_repoThrowsError_passesError() {
        // Arrange
        every { repository.loadTextFromExistingDetail(existingDetail) } returns Single.error(
            RuntimeException()
        )

        // Act & Assert
        loadTextUseCase.buildUseCaseSingle(
            LoadTextDetailUseCase.LoadTextParams(existingDetail)
        ).test()
            .assertError(RuntimeException::class.java)
    }
}