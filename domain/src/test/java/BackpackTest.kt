import detail.AudioDetail
import io.mockk.mockk
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class BackpackTest {

    private lateinit var backpack: Backpack
    private lateinit var detail: AudioDetail

    @Before
    fun setUp() {
        backpack = Backpack()
        detail = mockk()
    }

    @Test
    fun `Backpack addDetail correctly adds the detail`() {
        backpack.addDetail(detail)

        Assert.assertEquals(1, backpack.details.size)
        Assert.assertEquals(detail, backpack.details.first())
    }

    @Test
    fun `Backpack removeDetail correctly removes the detail`() {
        backpack.removeDetail(detail)

        Assert.assertEquals(0, backpack.details.size)
    }
}