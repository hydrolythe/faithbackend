package be.hogent.faith.faith.service.backpack

import Backpack
import User
import detail.Detail
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import be.hogent.faith.faith.service.BackpackService
import usecases.detailscontainer.DeleteDetailsContainerDetailUseCase
import usecases.detailscontainer.GetDetailsContainerDataUseCase
import usecases.detailscontainer.LoadDetailFileUseCase

class BackpackServiceDeleteDetailTest {
    private lateinit var viewModel: BackpackService
    private val deleteBackpackDetailUseCase =
        mockk<DeleteDetailsContainerDetailUseCase<Backpack>>(relaxed = true)
    private val loadDetailFileUseCase = mockk<LoadDetailFileUseCase<Backpack>>(relaxed = true)
    private val getBackPackFilesUseCase = mockk<GetDetailsContainerDataUseCase<Backpack>>(relaxed = true)
    private val backpack = mockk<Backpack>(relaxed = true)
    private val detail = mockk<Detail>()
    private val user = mockk<User>()

    @Before
    fun setUp() {
        viewModel = BackpackService(
            mockk(),
            deleteBackpackDetailUseCase,
            backpack,
            loadDetailFileUseCase,
            getBackPackFilesUseCase
        )
    }

    @Test
    fun backpackService_deleteDetail_callsUseCase() {
        val params = slot<DeleteDetailsContainerDetailUseCase.Params>()

        viewModel.deleteDetail(user.uuid,detail)
        verify { deleteBackpackDetailUseCase.execute(capture(params), any()) }

        assertEquals(detail, params.captured.detail)
    }
}