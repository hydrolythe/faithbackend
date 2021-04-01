package usecases.goal

import User
import encryption.IGoalEncryptionService
import goals.Goal
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import repository.IGoalRepository
import usecases.base.CompletableUseCase

class UpdateGoalUseCase(
    private val goalEncryptionService: IGoalEncryptionService,
    private val goalRepository: IGoalRepository,
    observer: Scheduler
) : CompletableUseCase<UpdateGoalUseCase.Params>(observer) {

    override fun buildUseCaseObservable(params: Params): Completable {
        params.user.updateGoal(params.goal)
        return goalEncryptionService
            .encrypt(params.goal)
            .doOnSuccess {  }
            .flatMapCompletable{ goal -> goalRepository.update(goal,params.user.uuid) }
            .doOnComplete {  }
            .doOnError {  }
    }

    data class Params(
        var goal: Goal,
        var user: User
    )
}