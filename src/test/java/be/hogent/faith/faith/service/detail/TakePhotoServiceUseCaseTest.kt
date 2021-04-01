package be.hogent.faith.faith.service.detail

import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import be.hogent.faith.faith.service.TakePhotoService
import usecases.detail.photodetail.CreatePhotoDetailUseCase
import java.io.File

class TakePhotoServiceUseCaseTest {
    private lateinit var viewModel: TakePhotoService
    private val createPhotoDetailUseCase = mockk<CreatePhotoDetailUseCase>(relaxed = true)
    private val file = mockk<File>()

    @Before
    fun setUp() {
        viewModel = TakePhotoService(createPhotoDetailUseCase)
    }

    @Test
    fun takePhotoViewModel_onSaveClicked_callsUseCase() {
        // Arrange
        // We need to save a picture before we can call the use case

        // Act
        viewModel.onSaveClicked(file)
        verify { createPhotoDetailUseCase.execute(any(), any()) }
    }
}