package be.hogent.faith.faith.iservice

import User
import goals.Goal

interface IGoalService{
    fun onSaveButtonClicked(givenGoal: Goal, user: User)
}