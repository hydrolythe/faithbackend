package event

import factory.DetailFactory
import factory.EventFactory
import io.mockk.clearAllMocks
import io.mockk.mockk
import junit.framework.Assert.assertFalse
import org.junit.After
import org.junit.Before
import org.junit.Test
import usecases.event.DeleteEventDetailUseCase

class DeleteEventDetailUseCaseTest {

    private lateinit var useCase: DeleteEventDetailUseCase

    private val event = EventFactory.makeEvent()
    private val detail = DetailFactory.makeRandomDetail()

    @Before
    fun setUp() {
        event.addDetail(detail)
        useCase = DeleteEventDetailUseCase(observer = mockk())
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `The detail is only deleted from the event`() {
        useCase.buildUseCaseObservable(DeleteEventDetailUseCase.Params(detail, event))
            .test()
            .assertComplete()
            .assertNoErrors()

        assertFalse(event.details.contains(detail))
    }
}