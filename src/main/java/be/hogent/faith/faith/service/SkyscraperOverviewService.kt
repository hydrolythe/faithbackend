package be.hogent.faith.faith.service

import User
import be.hogent.faith.faith.iservice.ISkyscraperOverviewService
import exception.MaxNumberOfGoalsReachedException
import goals.Goal
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import io.reactivex.rxjava3.subscribers.DisposableSubscriber
import org.springframework.stereotype.Service
import usecases.goal.AddNewGoalUseCase
import usecases.goal.GetGoalsUseCase
import usecases.goal.SaveGoalUseCase
import usecases.goal.UpdateGoalUseCase

class SkyscraperOverviewService(
    private val getGoalsUseCase: GetGoalsUseCase,
    private val addNewGoalUseCase: AddNewGoalUseCase,
    private val updateGoalUseCase: UpdateGoalUseCase,
) : ISkyscraperOverviewService{
    var mutableGoals = listOf<Goal>()
    var vlag = false

    override fun loadGoals(user:User):List<Goal> {
        val params = GetGoalsUseCase.Params(user)
        getGoalsUseCase.execute(params, object : DisposableSubscriber<List<Goal>>() {
            override fun onComplete() {
                vlag = true
            }

            override fun onNext(goals: List<Goal>?) {
                if (goals != null) {
                    mutableGoals = goals
                }
            }

            override fun onError(error: Throwable?) {
                vlag = true
            }
        })
        return mutableGoals
    }

    override fun addNewGoal(user:User) {
        val params = AddNewGoalUseCase.Params(user)
        addNewGoalUseCase.execute(params, object : DisposableCompletableObserver() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {

            }
        })
    }

    override fun updateGoalDescription(goal: Goal, newDescription: String, user:User) {
        goal.description = newDescription
        val params = UpdateGoalUseCase.Params(goal, user)
        //  startLoading()
        updateGoalUseCase.execute(params, object : DisposableCompletableObserver() {
            override fun onComplete() {
                //  doneLoading()
            }

            override fun onError(e: Throwable) {
                // doneLoading()
            }
        })
    }
}