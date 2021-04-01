package goal

import Mapper
import encryption.EncryptedGoal
import java.util.*

internal object GoalMapper :
    Mapper<EncryptedGoalEntity, EncryptedGoal> {

    override fun mapFromEntity(entity: EncryptedGoalEntity): EncryptedGoal {
        return EncryptedGoal(
            dateTime = entity.dateTime,
            description = entity.description,
            uuid = UUID.fromString(entity.uuid),
            isCompleted = entity.completed,
            currentPositionAvatar = entity.currentPositionAvatar,
            goalColor = entity.color,
            reachGoalWay = entity.reachGoalWay,
            subgoals = SubGoalMapper.mapFromEntities(
                entity.subgoals
            ),
            encryptedDEK = entity.encryptedDEK
        )
    }

    override fun mapToEntity(model: EncryptedGoal): EncryptedGoalEntity {
        return EncryptedGoalEntity(
            dateTime = model.dateTime,
            description = model.description,
            uuid = model.uuid.toString(),
            completed = model.isCompleted,
            currentPositionAvatar = model.currentPositionAvatar,
            color = model.goalColor,
            reachGoalWay = model.reachGoalWay,
            subgoals = SubGoalMapper.mapToEntities(
                model.subgoals.toList()
            ),
            encryptedDEK = model.encryptedDEK
        )
    }

    override fun mapToEntities(models: List<EncryptedGoal>): List<EncryptedGoalEntity> {
        return models.map(::mapToEntity)
    }

    override fun mapFromEntities(entities: List<EncryptedGoalEntity>): List<EncryptedGoal> {
        return entities.map(::mapFromEntity)
    }
}
