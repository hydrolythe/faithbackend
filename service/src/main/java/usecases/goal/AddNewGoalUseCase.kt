package usecases.goal

import User
import encryption.IGoalEncryptionService
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import repository.IGoalRepository
import usecases.base.CompletableUseCase

open class AddNewGoalUseCase(
    private val goalEncryptionService: IGoalEncryptionService,
    private val goalRepository: IGoalRepository,
    observer: Scheduler
) : CompletableUseCase<AddNewGoalUseCase.Params>(observer) {

    private var params: Params? = null

    override fun buildUseCaseObservable(params: Params): Completable {
        return Single.just(params.user)
            .map { user -> user.addNewGoal() }
            .flatMap { newGoal -> goalEncryptionService.encrypt(newGoal) }
            .doOnSuccess {  }
            .flatMapCompletable{ result -> goalRepository.update(result, params.user.uuid) }
            .doOnComplete{  }
    }

    data class Params(
        val user:User
    )
}
