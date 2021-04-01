package usecases.goal

import User
import goals.Goal
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import repository.IGoalRepository
import usecases.base.CompletableUseCase

class DeleteGoalUseCase(
    private val goalRepository: IGoalRepository,
    observer: Scheduler
) : CompletableUseCase<DeleteGoalUseCase.Params>(observer) {

    override fun buildUseCaseObservable(params: Params): Completable {
        return goalRepository.delete(params.goal.uuid,params.user)
    }

    data class Params(val goal: Goal,val user:String)
}
