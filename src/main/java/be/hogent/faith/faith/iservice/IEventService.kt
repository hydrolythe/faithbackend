package be.hogent.faith.faith.iservice

import Event
import detail.*
import java.awt.image.BufferedImage

interface IEventService {
    fun saveEmotionAvatarImage(bitmap: BufferedImage,givenEvent:Event)
    fun saveCurrentDetail(detail:Detail, event:Event)
    fun saveAudioDetail(audioDetail: AudioDetail,givenEvent:Event)
    fun savePhotoDetail(photoDetail: PhotoDetail,givenEvent:Event)
    fun saveTextDetail(detail: TextDetail,givenEvent:Event)
    fun saveDrawingDetail(detail: DrawingDetail,givenEvent:Event)
    fun deleteDetail(detail:Detail,givenEvent:Event)
}