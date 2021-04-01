package usecases.goal

import User
import encryption.IGoalEncryptionService
import goals.Goal
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import repository.IGoalRepository
import usecases.base.CompletableUseCase

open class SaveGoalUseCase(
    private val goalEncryptionService: IGoalEncryptionService,
    private val goalRepository: IGoalRepository,
    observer: Scheduler
) : CompletableUseCase<SaveGoalUseCase.Params>(observer) {

    override fun buildUseCaseObservable(params: SaveGoalUseCase.Params): Completable {
        return goalEncryptionService.encrypt(params.goal)
            .doOnSuccess {  }
            .flatMapCompletable{ goal -> goalRepository.insert(goal,params.uuid) }
            .doOnComplete {  }
    }

    data class Params(
        var goal: Goal,
        var uuid: String
    )
}
