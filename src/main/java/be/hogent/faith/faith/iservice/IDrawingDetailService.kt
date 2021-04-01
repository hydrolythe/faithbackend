package be.hogent.faith.faith.iservice

import detail.DrawingDetail
import java.awt.image.BufferedImage

interface IDrawingDetailService {
    fun loadExistingDetail(existingDetail:DrawingDetail)
    fun onBitMapAvailable(bitmap: BufferedImage):DrawingDetail
}