package factory

import detail.*

object DetailFactory {

    fun makeRandomDetail(): Detail {
        val rand = Math.random()
        return when {
            rand < 0.33 -> makeTextDetail()
            rand < 0.66 -> makeDrawingDetail()
            else -> makeAudioDetail()
        }
    }

    fun makePhotoDetail(): PhotoDetail =
        PhotoDetail(DataFactory.randomFile(), DataFactory.randomString(), "", DataFactory.randomUUID())

    fun makeTextDetail(): TextDetail =
        TextDetail(DataFactory.randomFile(), "", DataFactory.randomUUID())

    fun makeDrawingDetail(): DrawingDetail =
        DrawingDetail(DataFactory.randomFile(), DataFactory.randomString(), "", DataFactory.randomUUID())

    fun makeAudioDetail(): AudioDetail =
        AudioDetail(DataFactory.randomFile(), "", DataFactory.randomUUID())
}