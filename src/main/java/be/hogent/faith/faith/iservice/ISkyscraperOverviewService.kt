package be.hogent.faith.faith.iservice

import User
import goals.Goal

interface ISkyscraperOverviewService {
    fun loadGoals(user:User):List<Goal>
    fun addNewGoal(user: User)
    fun updateGoalDescription(goal: Goal, newDescription: String, user:User)
}