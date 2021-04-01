package usecases.detail.audiodetail

import detail.AudioDetail
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import usecases.base.SingleUseCase
import java.io.File

class CreateAudioDetailUseCase(
    observeScheduler: Scheduler
) : SingleUseCase<AudioDetail, CreateAudioDetailUseCase.Params>(observeScheduler) {

    override fun buildUseCaseSingle(params: Params): Single<AudioDetail> {
        return Single.
        fromCallable {
            AudioDetail(params.tempAudioSaveFile)
        }
    }

    class Params(
        val tempAudioSaveFile: File
    )
}