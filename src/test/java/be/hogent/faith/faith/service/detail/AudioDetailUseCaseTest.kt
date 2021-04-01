package be.hogent.faith.faith.service.detail

import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import be.hogent.faith.faith.service.AudioDetailService
import usecases.detail.audiodetail.CreateAudioDetailUseCase
import java.io.File

class AudioDetailUseCaseTest {
    private lateinit var viewModel: AudioDetailService
    private val createAudioDetailUseCase = mockk<CreateAudioDetailUseCase>(relaxed = true)
    private val file = mockk<File>()

    @Before
    fun setUp() {
        viewModel = AudioDetailService(createAudioDetailUseCase)
    }

    @Test
    fun takePhotoViewModel_onSaveClicked_callsUseCase() {
        // Arrange
        // We need to save a picture before we can call the use case

        // Act
        viewModel.onSaveClicked(file)
        verify { createAudioDetailUseCase.execute(any(), any()) }
    }
}