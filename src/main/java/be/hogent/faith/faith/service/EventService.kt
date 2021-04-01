package be.hogent.faith.faith.service

import Event
import User
import be.hogent.faith.faith.iservice.IEventService
import detail.*
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import org.springframework.stereotype.Service
import usecases.event.DeleteEventDetailUseCase
import usecases.event.SaveEmotionAvatarUseCase
import usecases.event.SaveEventDetailUseCase
import java.awt.image.BufferedImage

class EventService(
    private val saveEmotionAvatarUseCase: SaveEmotionAvatarUseCase,
    private val saveEventDetailUseCase: SaveEventDetailUseCase,
    private val deleteDetailUseCase: DeleteEventDetailUseCase
): IEventService {
    override fun saveCurrentDetail(detail: Detail, event:Event) {
        when (detail) {
            is DrawingDetail -> saveDrawingDetail(detail,event)
            is TextDetail -> saveTextDetail(detail,event)
            is PhotoDetail -> savePhotoDetail(detail,event)
            is AudioDetail -> saveAudioDetail(detail,event)
            else -> throw IllegalArgumentException("Te doen: Video en YoutubeVideo moeten nog toegevoegd worden.")
        }
    }

    override fun saveEmotionAvatarImage(bitmap: BufferedImage,givenEvent:Event) {
        val params = SaveEmotionAvatarUseCase.Params(bitmap, givenEvent)
        saveEmotionAvatarUseCase.execute(params, object : DisposableCompletableObserver() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {

            }
        })
    }

    override fun saveAudioDetail(audioDetail: AudioDetail,givenEvent:Event) {
        val params = SaveEventDetailUseCase.Params(audioDetail, givenEvent)
        saveEventDetailUseCase.execute(params, object : DisposableCompletableObserver() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {

            }
        })
    }

    override fun savePhotoDetail(photoDetail: PhotoDetail,givenEvent:Event) {
        val params = SaveEventDetailUseCase.Params(photoDetail, givenEvent)
        saveEventDetailUseCase.execute(params, object : DisposableCompletableObserver() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {

            }
        })
    }

    override fun saveTextDetail(detail: TextDetail,givenEvent:Event) {
        val params = SaveEventDetailUseCase.Params(detail, givenEvent)
        saveEventDetailUseCase.execute(params, object : DisposableCompletableObserver() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {

            }
        })
    }

    override fun saveDrawingDetail(detail: DrawingDetail,givenEvent:Event) {
        val params = SaveEventDetailUseCase.Params(detail, givenEvent)
        saveEventDetailUseCase.execute(params, object : DisposableCompletableObserver() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {

            }
        })
    }

    override fun deleteDetail(detail: Detail,givenEvent:Event) {
        val params = DeleteEventDetailUseCase.Params(detail, givenEvent)
        deleteDetailUseCase.execute(params, object : DisposableCompletableObserver() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {

            }
        })
    }
}