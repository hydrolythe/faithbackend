package usecases.event

import Event
import User
import encryption.EncryptedEvent
import encryption.IEventEncryptionService
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import repository.IEventRepository
import usecases.base.FlowableUseCase

/**
 * Returns all the events associated with a user.
 */
class GetEventsUseCase(
    private val eventRepository: IEventRepository,
    private val eventEncryptionService: IEventEncryptionService,
    observer: Scheduler,
    subscriber: Scheduler = Schedulers.io()
) : FlowableUseCase<List<Event>, GetEventsUseCase.Params>(observer, subscriber) {

    override fun buildUseCaseObservable(params: Params): Flowable<List<Event>> {
        return eventRepository.getAll(params.uuid)
            .doOnSubscribe {  }
            .doOnNext {  }
            .concatMapSingle { list ->
                Observable.fromIterable(list)
                    .flatMapSingle { encryptedEvent ->
                        eventEncryptionService.decryptData(encryptedEvent)
                            .subscribeOn(subscriber)
                            .doOnSuccess {  }
                    }
                    .toList()
                    .doOnSuccess {  }
            }
    }

    data class Params(
        val uuid: String
    )
}