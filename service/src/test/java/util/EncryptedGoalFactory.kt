package util

import encryption.EncryptedAction
import encryption.EncryptedGoal
import encryption.EncryptedSubGoal
import factory.DataFactory
import goals.ActionStatus
import goals.GoalColor
import goals.ReachGoalWay

object EncryptedGoalFactory {
    fun makeGoal(numberOfSubGoals: Int = 5): EncryptedGoal {
        return EncryptedGoal(
            dateTime = DataFactory.randomDateTime().toString(),
            description = DataFactory.randomString(),
            isCompleted = DataFactory.randomBoolean().toString(),
            uuid = DataFactory.randomUUID(),
            encryptedDEK = DataFactory.randomString(),
            currentPositionAvatar = DataFactory.randomInt(0, 9),
            goalColor = GoalColor.GREEN.name,
            reachGoalWay = ReachGoalWay.Elevator.name,
            subgoals = makeSubGoalList(numberOfSubGoals)
        )
    }

    fun makeGoalList(count: Int = 5): List<EncryptedGoal> {
        val events = mutableListOf<EncryptedGoal>()
        repeat(count) {
            events.add(makeGoal())
        }
        return events
    }

    fun makeSubGoalList(count: Int = 5): List<EncryptedSubGoal> {
        val subgoals = mutableListOf<EncryptedSubGoal>()
        repeat(count) {
            subgoals.add(
                EncryptedSubGoal(
                    it, DataFactory.randomString(10), makeActionList(5)
                )
            )
        }
        return subgoals
    }

    fun makeActionList(count: Int = 5): List<EncryptedAction> {
        val actions = mutableListOf<EncryptedAction>()
        repeat(count) {
            actions.add(EncryptedAction(DataFactory.randomString(10), ActionStatus.ACTIVE.name))
        }
        return actions
    }
}