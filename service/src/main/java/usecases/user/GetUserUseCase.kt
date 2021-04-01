package usecases.user

import Event
import User
import encryption.EncryptedEvent
import encryption.IEventEncryptionService
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.kotlin.Flowables
import io.reactivex.rxjava3.schedulers.Schedulers
import repository.IAuthManager
import repository.IEventRepository
import repository.IUserRepository
import usecases.base.FlowableUseCase

class GetUserUseCase(
    private val userRepository: IUserRepository,
    private val eventRepository: IEventRepository,
    private val eventEncryptionService: IEventEncryptionService,
    private val authManager: IAuthManager,
    observeScheduler: Scheduler,
    subscriber: Scheduler = Schedulers.io()
) : FlowableUseCase<User, GetUserUseCase.Params>(observeScheduler, subscriber) {

    override fun buildUseCaseObservable(params: Params): Flowable<User> {
        val currentUser = params.uuid
        return Flowables.combineLatest(
            userRepository.get(currentUser)
                .subscribeOn(subscriber),
            eventRepository.getAll(currentUser)
                .doOnError { println(it) }
                .subscribeOn(subscriber)
                .concatMapSingle { list ->
                    Observable.fromIterable(list)
                        .flatMapSingle {
                            eventEncryptionService.decryptData(it).subscribeOn(subscriber)
                        }
                        .toList()
                }

        )
            .map { pair: Pair<User, MutableList<Event>> ->
                addEventsToUser(pair.first, pair.second)
            }

    }

    private fun addEventsToUser(user: User, events: @NonNull MutableList<Event>): User {
        user.clearEvents()
        events.forEach { user.addEvent(it) }
        return user
    }

    class Params(val uuid:String)
}
