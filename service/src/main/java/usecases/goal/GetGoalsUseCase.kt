package usecases.goal

import User
import encryption.EncryptedGoal
import encryption.IGoalEncryptionService
import goals.Goal
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import repository.IGoalRepository
import usecases.base.FlowableUseCase

class GetGoalsUseCase(
    private val goalRepository: IGoalRepository,
    private val goalEncryptionService: IGoalEncryptionService,
    observer: Scheduler,
    subscriber: Scheduler = Schedulers.io()
) : FlowableUseCase<List<Goal>, GetGoalsUseCase.Params>(observer, subscriber) {

    override fun buildUseCaseObservable(params:Params): Flowable<List<Goal>> {
        return goalRepository
            .getAll(params.user.uuid)
            .subscribeOn(subscriber)
            .doOnEach {  }
            .doOnError {
                it.printStackTrace()
            }
            .concatMapSingle { list ->
                Observable.fromIterable(list)
                    .flatMapSingle { encryptedGoal ->
                        goalEncryptionService.decryptData(encryptedGoal)
                            .subscribeOn(subscriber)
                            .doOnSuccess {  }
                            .doOnError {
                                it.printStackTrace()
                            }
                    }
                    .toList()
                    .doOnSuccess {
                        params.user.setGoals(it)
                    }
            }
    }

    data class Params(val user: User)
}