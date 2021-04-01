package factory

import common.EncryptedDetailEntity
import converters.FileConverter
import converters.LocalDateTimeConverter
import event.EncryptedEventEntity
import goal.EncryptedActionEntity
import goal.EncryptedGoalEntity
import goal.EncryptedSubGoalEntity
import goals.GoalColor
import goals.ReachGoalWay
import user.UserEntity
import java.util.*

object EntityFactory {

    fun makeDetailEntity(): EncryptedDetailEntity {
        val rand = Math.random()
        return when {
            rand < 0.25 -> EncryptedDetailEntity(
                FileConverter().toString(DataFactory.randomFile()),
                "",
                DataFactory.randomUUID().toString(),
                "photo"
            )
            rand < 0.50 -> EncryptedDetailEntity(
                FileConverter().toString(DataFactory.randomFile()),
                "",
                DataFactory.randomUUID().toString(),
                "drawing"
            )
            rand < 0.75 -> EncryptedDetailEntity(
                FileConverter().toString(DataFactory.randomFile()),
                "",
                DataFactory.randomUUID().toString(),
                "audio"
            )
            else -> EncryptedDetailEntity(
                FileConverter().toString(DataFactory.randomFile()),
                "",
                DataFactory.randomUUID().toString(),
                "text"
            )
        }
    }

    fun makeDetailEntityList(count: Int): List<EncryptedDetailEntity> {
        val details = mutableListOf<EncryptedDetailEntity>()
        repeat(count) {
            details.add(makeDetailEntity())
        }
        return details
    }

    fun makeEventEntity(uuid: UUID = DataFactory.randomUUID()): EncryptedEventEntity {
        return EncryptedEventEntity(
            dateTime = LocalDateTimeConverter().toString(DataFactory.randomDateTime()),
            title = DataFactory.randomString(),
            emotionAvatar = FileConverter().toString(DataFactory.randomFile()),
            notes = DataFactory.randomString(),
            uuid = uuid.toString(),
            encryptedStreamingDEK = "sDEK",
            encryptedDEK = "DEK"
        )
    }

    fun makeEventEntityListWithDetails(count: Int): List<EncryptedEventEntity> {
        val events = mutableListOf<EncryptedEventEntity>()
        repeat(count) {
            events.add(makeEventEntityWithDetails())
        }
        return events
    }

    fun makeEventEntityWithDetails(nbrOfDetails: Int = 5): EncryptedEventEntity {
        return EncryptedEventEntity(
            dateTime = LocalDateTimeConverter().toString(DataFactory.randomDateTime()),
            title = DataFactory.randomString(),
            emotionAvatar = FileConverter().toString(DataFactory.randomFile()),
            notes = DataFactory.randomString(),
            uuid = DataFactory.randomUUID().toString(),
            details = makeDetailEntityList(nbrOfDetails),
            encryptedStreamingDEK = "sDEK",
            encryptedDEK = "DEK"
        )
    }

    fun makeGoalEntity(uuid: UUID = DataFactory.randomUUID()): EncryptedGoalEntity {
        return EncryptedGoalEntity(
            dateTime = LocalDateTimeConverter().toString(DataFactory.randomDateTime()),
            description = DataFactory.randomString(),
            uuid = uuid.toString(),
            completed = DataFactory.randomBoolean().toString(),
            currentPositionAvatar = DataFactory.randomInt(1, 10),
            color = GoalColor.GREEN.name,
            reachGoalWay = ReachGoalWay.Elevator.name,
            subgoals = makeSubGoalEntityList(10),
            encryptedDEK = "DEK"
        )
    }

    fun makeSubGoalEntity(sequenceNumber: Int): EncryptedSubGoalEntity {
        return EncryptedSubGoalEntity(
            sequenceNumber = sequenceNumber,
            description = DataFactory.randomString(),
            actions = makeActionEntityList(5)
        )
    }

    fun makeSubGoalEntityList(count: Int): List<EncryptedSubGoalEntity> {
        val subgoals = mutableListOf<EncryptedSubGoalEntity>()
        repeat(count) {
            subgoals.add(makeSubGoalEntity(it - 1))
        }
        return subgoals
    }

    fun makeActionEntity(): EncryptedActionEntity {
        return EncryptedActionEntity(
            description = DataFactory.randomString(),
            currentStatus = DataFactory.randomString()
        )
    }

    fun makeActionEntityList(count: Int): List<EncryptedActionEntity> {
        val actions = mutableListOf<EncryptedActionEntity>()
        repeat(count) {
            actions.add(makeActionEntity())
        }
        return actions
    }

    fun makeUserEntity(): UserEntity {
        return UserEntity(
            DataFactory.randomUUID().toString(),
            DataFactory.randomString(),
            DataFactory.randomString()
        )
    }
}
