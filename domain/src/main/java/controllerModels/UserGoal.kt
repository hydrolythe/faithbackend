package controllerModels

import User
import goals.Goal

data class UserGoal(val givenGoal: Goal,val user: User)
