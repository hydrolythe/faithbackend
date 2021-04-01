package common

import encryption.EncryptedAction
import encryption.EncryptedGoal
import encryption.EncryptedSubGoal
import factory.DataFactory
import factory.EntityFactory
import factory.UserFactory
import goal.EncryptedActionEntity
import goal.EncryptedGoalEntity
import goal.EncryptedSubGoalEntity
import goal.GoalMapper
import junit.framework.Assert.assertEquals
import org.junit.Test

class GoalMapperTest {

    private val goalMapper = GoalMapper
    private val user = UserFactory.makeUser()

    private val encryptedAction = EncryptedAction(
        description = DataFactory.randomString(),
        currentStatus = DataFactory.randomString()
    )

    private val encryptedSubGoal = EncryptedSubGoal(
        sequenceNumber = DataFactory.randomInt(0, 9),
        description = DataFactory.randomString(),
        actions = listOf(encryptedAction)
    )

    private val encryptedGoal = EncryptedGoal(
        description = DataFactory.randomString(),
        uuid = DataFactory.randomUUID(),
        dateTime = DataFactory.randomString(),
        isCompleted = DataFactory.randomBoolean().toString(),
        currentPositionAvatar = DataFactory.randomInt(1, 10),
        goalColor = DataFactory.randomString(),
        subgoals = listOf(encryptedSubGoal),
        reachGoalWay = DataFactory.randomString(),
        encryptedDEK = "encrypted version of DEK"
    )

    @Test
    fun goalMapper_mapFromEntity() {
        val goalEntity = EntityFactory.makeGoalEntity(DataFactory.randomUUID())
        val resultingGoal = goalMapper.mapFromEntity(goalEntity)
        assertEqualData(goalEntity, resultingGoal)
    }

    @Test
    fun goalMapper_mapToEntity() {
        val resultingGoalEntity = goalMapper.mapToEntity(encryptedGoal)
        assertEqualData(resultingGoalEntity, encryptedGoal)
    }

    private fun assertEqualData(
        entity: EncryptedGoalEntity,
        model: EncryptedGoal
    ) {
        assertEquals(entity.uuid, model.uuid.toString())
        assertEquals(entity.dateTime, model.dateTime)
        assertEquals(entity.description, model.description)
        assertEquals(entity.completed, model.isCompleted)
        assertEquals(entity.currentPositionAvatar, model.currentPositionAvatar)
        assertEquals(entity.color, model.goalColor)
        assertEquals(entity.reachGoalWay, model.reachGoalWay)
        assertEquals(entity.encryptedDEK, model.encryptedDEK)
        for ((index, value) in entity.subgoals.withIndex())
            assertEqualSubGoalData(value, model.subgoals[index])
    }

    private fun assertEqualSubGoalData(
        entity: EncryptedSubGoalEntity,
        model: EncryptedSubGoal
    ) {
        assertEquals(entity.sequenceNumber, model.sequenceNumber)
        assertEquals(entity.description, model.description)
        for ((index, value) in entity.actions.withIndex())
            assertEqualActionData(value, model.actions[index])
    }

    private fun assertEqualActionData(
        entity: EncryptedActionEntity,
        model: EncryptedAction
    ) {
        assertEquals(entity.description, model.description)
        assertEquals(entity.currentStatus, model.currentStatus)
    }
}
