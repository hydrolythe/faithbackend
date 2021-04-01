package thumbnail

import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam

const val THUMBNAIL_WIDTH = 60
const val THUMBNAIL_HEIGHT = 60

class ThumbnailProvider() {
    fun getBase64EncodedThumbnail(file: File): String {
        val image = ImageIO.read(file)
        return getBase64EncodedThumbnail(image)
    }

    fun getBase64EncodedThumbnail(image: BufferedImage): String {
        val bufferedImage = BufferedImage(THUMBNAIL_WIDTH,THUMBNAIL_HEIGHT,BufferedImage.TYPE_INT_RGB)
        val graphics = bufferedImage.createGraphics()
        graphics.drawImage(image, 0, 0, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, null)
        graphics.dispose()
        return encodebase64(
            bufferedImage
        )
    }

    private fun encodebase64(image: BufferedImage): String {
        val baos = ByteArrayOutputStream()
        val oos = ImageIO.createImageOutputStream(baos)
        val jpgWriter = ImageIO.getImageWritersByFormatName("JPEG").next()
        val jpgWriteParam = jpgWriter.defaultWriteParam
        jpgWriteParam.compressionMode = ImageWriteParam.MODE_EXPLICIT
        jpgWriteParam.compressionQuality = 0.7f
        jpgWriter.output = oos
        jpgWriter.write(null, IIOImage(image,null,null),jpgWriteParam)
        jpgWriter.dispose()
        val b = baos.toByteArray()
        return Base64.getEncoder().encodeToString(b)
    }
}