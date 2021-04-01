package usecases.event

import Event
import User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import repository.IEventRepository
import usecases.base.CompletableUseCase

class DeleteEventUseCase(
    private val eventRepository: IEventRepository,
    observer: Scheduler
) : CompletableUseCase<DeleteEventUseCase.Params>(observer) {

    override fun buildUseCaseObservable(params: Params): Completable {
        return (eventRepository.delete(params.event, params.user.uuid)
                .subscribeOn(subscriber))
            .andThen(Completable.fromAction {
                params.user.removeEvent(params.event)
            })
    }

    data class Params(
        val event: Event,
        val user: User
    )
}