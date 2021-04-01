package be.hogent.faith.faith.service

import User
import be.hogent.faith.faith.iservice.IGoalService
import goals.Goal
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import org.springframework.stereotype.Service
import usecases.goal.UpdateGoalUseCase

class GoalService(
    private val updateGoalUseCase: UpdateGoalUseCase
): IGoalService
{
    override fun onSaveButtonClicked(givenGoal:Goal,user:User) {
        val params = UpdateGoalUseCase.Params(givenGoal, user)
        updateGoalUseCase.execute(params, object : DisposableCompletableObserver() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {

            }
        })
    }
}