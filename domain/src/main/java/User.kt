import encryption.EncryptedGoal
import exception.MaxNumberOfGoalsReachedException
import goals.Goal
import goals.GoalColor
import java.util.*

const val MAX_NUMBER_OF_ACTIVE_GOALS = 5

data class User(
    val username: String,
    /**
     * The name of the avatar this user chose.
     * This should be unique, and will be used to request the image corresponding to the chosen avatar.
     */
    var avatarName: String,
    // generated by Firebase, and they don't generate valid UUID's
    val uuid: String
) {
    private var _events = HashMap<UUID, Event>()
    val events: List<Event>
        get() = _events.values.toList()

    private var allGoals = mutableListOf<Goal>()

    val achievedGoals: List<Goal>
        get() = allGoals.filter { it.isCompleted }

    val activeGoals: List<Goal>
        get() = allGoals.filter { it.isCompleted.not() }

    val backpack = Backpack()

    val cinema = Cinema()

    val treasureChest = TreasureChest()

    fun addEvent(event: Event) {
        if (event.title.isNullOrBlank()) {
            throw IllegalArgumentException("Een gebeurtenis moet een ingevulde titel hebben.")
        }
        _events[event.uuid] = event
    }

    fun getEvent(eventUUID: UUID): Event? {
        return _events[eventUUID]
    }

    fun clearEvents() {
        _events = HashMap()
    }

    fun removeEvent(event: Event) {
        _events.remove(event.uuid)
    }

    fun addNewGoal(): Goal {
        if (activeGoals.size >= MAX_NUMBER_OF_ACTIVE_GOALS) {
            throw MaxNumberOfGoalsReachedException()
        }
        val uniqueColor = findUnusedColor()
        val goal = Goal(uniqueColor)
        allGoals.add(goal)
        return goal
    }

    fun updateGoal(goal: Goal) {
        allGoals[allGoals.indexOf(getGoal(goal.uuid))] = goal
    }

    fun removeGoal(goal: Goal) {
        allGoals.remove(goal)
    }

    fun getGoal(goalUUID: UUID): Goal? {
        return allGoals.filter { it.uuid == goalUUID }.firstOrNull()
    }

    /**
     * Uses the list of [GoalColor]s to find a color that is not currently used in the [activeGoals].
     */
    private fun findUnusedColor(): GoalColor {
        val currentlyUsedColors = activeGoals.map { it.goalColor }
        val availableColors = GoalColor.values().subtract(currentlyUsedColors)
        return availableColors.first()
    }

    fun setGoalCompleted(goal: Goal) {
        goal.toggleCompleted()
    }

    fun setGoals(goals: MutableList<Goal>) {
        allGoals = goals.toMutableList()
    }
}
