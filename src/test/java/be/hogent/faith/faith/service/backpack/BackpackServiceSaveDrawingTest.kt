package be.hogent.faith.faith.service.backpack

import Backpack
import User
import detail.DrawingDetail
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

class BackpackServiceSaveDrawingTest {

    private lateinit var viewModel: BackpackService
    private val saveDrawingUseCase =
        mockk<SaveDetailsContainerDetailUseCase<Backpack>>(relaxed = true)
    private val getBackPackFilesUseCase =
        mockk<GetDetailsContainerDataUseCase<Backpack>>(relaxed = true)
    private val backpack = mockk<Backpack>(relaxed = true)
    private val loadDetailFileUseCase = mockk<LoadDetailFileUseCase<Backpack>>(relaxed = true)
    private val detail = mockk<DrawingDetail>()
    private val user = mockk<User>(relaxed = true)


    @Before
    fun setUp() {
        viewModel = BackpackService(
            saveDrawingUseCase,
            mockk(),
            backpack,
            loadDetailFileUseCase,
            getBackPackFilesUseCase,
        )
    }

    @Test
    fun backpackViewModel_saveDrawing_callsUseCase() {
        val params = slot<SaveDetailsContainerDetailUseCase.Params>()

        viewModel.saveDrawingDetail(user.uuid, detail)
        verify { saveDrawingUseCase.execute(capture(params), any()) }

        assertEquals(detail, params.captured.detail)
    }
}