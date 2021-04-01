import detail.AudioDetail
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import java.time.Month

class EventTest {

    private lateinit var event: Event

    @Before
    fun setUp() {
        event = Event(mockk(), "testTitle")
    }

    @Test
    fun `Event constructor correctly sets attributes`() {
        val date = LocalDateTime.of(1991, Month.OCTOBER, 28, 8, 33)
        val title = "title"

        val newEvent = Event(date, title)

        assertEquals(title, newEvent.title)
        assertEquals(date, newEvent.dateTime)
    }

    @Test
    fun `Event addDetail correctly adds the detail`() {
        event.addNewAudioDetail(mockk())

        assertEquals(1, event.details.size)
        assertEquals(AudioDetail::class.java, event.details.first().javaClass)
    }

    @Test
    fun `Event getDetails returns all details`() {
        for (i in 1..10) {
            event.addNewTextDetail(mockk())
        }

        assertEquals(10, event.details.size)
    }
}