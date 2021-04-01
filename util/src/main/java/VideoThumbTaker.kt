
import javax.imageio.ImageIO

import org.bytedeco.javacv.FFmpegFrameGrabber
import java.io.*

import java.lang.Exception


class VideoThumbTaker() {


    fun createVideoThumbnail(file: File):File {
        val g = FFmpegFrameGrabber(file)
        g.format = "mp4"
        g.start()
        for (i in 0..49) {
            ImageIO.write(
                g.grab().bufferedImage,
                "png",
                file
            )
        }
        g.stop()
        return file
    }
}