package usecases.event

import Event
import User
import encryption.IEventEncryptionService
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import repository.IEventRepository
import thumbnail.ThumbnailProvider
import usecases.base.CompletableUseCase

open class SaveEventUseCase(
    private val eventEncryptionService: IEventEncryptionService,
    private val eventRepository: IEventRepository,
    private val thumbnailProvider: ThumbnailProvider,
    observer: Scheduler
) : CompletableUseCase<SaveEventUseCase.Params>(observer) {

    private var params: Params? = null

    override fun buildUseCaseObservable(params: Params): Completable {
        this.params = params
        if (params.event.emotionAvatar != null)
            params.event.emotionAvatarThumbnail = thumbnailProvider.getBase64EncodedThumbnail(params.event.emotionAvatar!!)
        return eventEncryptionService.encrypt(params.event)
            .doOnSuccess {  }
            .flatMapCompletable{ event -> eventRepository.insert(event,params.user.uuid) }
            .doOnComplete {  }
            .andThen(Completable.fromAction {
                with(params) {
                    user.addEvent(event)
                }
            })
    }

    data class Params(
        var event: Event,
        val user: User
    )
}
