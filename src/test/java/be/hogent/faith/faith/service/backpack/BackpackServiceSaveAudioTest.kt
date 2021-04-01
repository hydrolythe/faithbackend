package be.hogent.faith.faith.service.backpack

import Backpack
import User
import detail.AudioDetail
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import be.hogent.faith.faith.service.BackpackService
import usecases.detailscontainer.GetDetailsContainerDataUseCase
import usecases.detailscontainer.LoadDetailFileUseCase
import usecases.detailscontainer.SaveDetailsContainerDetailUseCase

class BackpackServiceSaveAudioTest {
    private lateinit var viewModel: BackpackService
    private val saveAudioUseCase =
        mockk<SaveDetailsContainerDetailUseCase<Backpack>>(relaxed = true)
    private val getBackPackFilesUseCase =
        mockk<GetDetailsContainerDataUseCase<Backpack>>(relaxed = true)
    private val backpack = mockk<Backpack>(relaxed = true)
    private val loadDetailFileUseCase = mockk<LoadDetailFileUseCase<Backpack>>(relaxed = true)
    private val detail = mockk<AudioDetail>()
    private val user = mockk<User>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = BackpackService(
            saveAudioUseCase,
            mockk(),
            backpack,
            loadDetailFileUseCase,
            getBackPackFilesUseCase
        )
    }

    @Test
    fun backpackViewModel_saveAudio_callsUseCase() {
        val params = slot<SaveDetailsContainerDetailUseCase.Params>()

        viewModel.saveAudioDetail(user.uuid, detail)
        verify { saveAudioUseCase.execute(capture(params), any()) }

        assertEquals(detail, params.captured.detail)
    }


}