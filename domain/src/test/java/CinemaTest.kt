import detail.DrawingDetail
import detail.FilmDetail
import io.mockk.mockk
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CinemaTest {

    private lateinit var cinema: Cinema
    private lateinit var detail: DrawingDetail
    private lateinit var film: FilmDetail

    @Before
    fun setUp() {
        cinema = Cinema()
        detail = mockk()
        film = mockk()
    }

    @Test
    fun `Cinema addDetail correctly adds the detail`() {
        cinema.addDetail(detail)

        Assert.assertEquals(1, cinema.details.size)
        Assert.assertEquals(detail, cinema.details.first())
    }

    @Test
    fun `Cinema removeDetail correctly removes the detail`() {
        cinema.removeDetail(detail)

        Assert.assertEquals(0, cinema.details.size)
    }

    @Test
    fun `Cinema addFilm correctly adds the film`() {
        cinema.addFilm(film)

        Assert.assertEquals(1, cinema.films.size)
        Assert.assertEquals(film, cinema.films.first())
    }

    @Test
    fun `Cinema removeFilm correctly removes the film`() {
        cinema.removeFilm(film)

        Assert.assertEquals(0, cinema.films.size)
    }
}