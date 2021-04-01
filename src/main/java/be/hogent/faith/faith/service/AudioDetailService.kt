package be.hogent.faith.faith.service

import be.hogent.faith.faith.iservice.IAudioDetailService
import detail.AudioDetail
import detail.Detail
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import org.springframework.stereotype.Service
import usecases.detail.audiodetail.CreateAudioDetailUseCase
import java.io.File

class AudioDetailService(
    private val createAudioDetailUseCase: CreateAudioDetailUseCase
): IAudioDetailService {

    private var existingDetail : AudioDetail? = null

    override fun onSaveClicked(file:File): AudioDetail {
        val params = CreateAudioDetailUseCase.Params(file)
        createAudioDetailUseCase.execute(params, CreateAudioDetailUseCaseHandler())
        while(existingDetail==null){
            Thread.sleep(1000)
        }
        return existingDetail!!
    }

    private inner class CreateAudioDetailUseCaseHandler :
        DisposableSingleObserver<AudioDetail>() {
        override fun onSuccess(createdDetail: AudioDetail) {
            existingDetail = createdDetail
        }

        override fun onError(e: Throwable) {

        }
    }
}