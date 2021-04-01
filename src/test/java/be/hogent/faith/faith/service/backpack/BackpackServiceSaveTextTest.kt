package be.hogent.faith.faith.service.backpack

import Backpack
import User
import detail.TextDetail
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

class BackpackServiceSaveTextTest {
    private lateinit var viewModel: BackpackService
    private val saveDetailsContainerDetailsUseCase =
        mockk<SaveDetailsContainerDetailUseCase<Backpack>>(relaxed = true)
    private val loadDetailFileUseCase = mockk<LoadDetailFileUseCase<Backpack>>(relaxed = true)
    private val getBackPackFilesUseCase =
        mockk<GetDetailsContainerDataUseCase<Backpack>>(relaxed = true)
    private val backpack = mockk<Backpack>(relaxed = true)
    private val detail = mockk<TextDetail>()
    private val user: User = mockk(relaxed = true)

    @Before
    fun setUp() {
        viewModel = BackpackService(
            saveDetailsContainerDetailsUseCase,
            mockk(),
            backpack,
            loadDetailFileUseCase,
            getBackPackFilesUseCase
        )
    }

    @Test
    fun backpackViewModel_saveText_callsUseCase() {
        val params = slot<SaveDetailsContainerDetailUseCase.Params>()

        viewModel.saveTextDetail(user.uuid, detail)
        verify { saveDetailsContainerDetailsUseCase.execute(capture(params), any()) }

        assertEquals(detail, params.captured.detail)
    }


}