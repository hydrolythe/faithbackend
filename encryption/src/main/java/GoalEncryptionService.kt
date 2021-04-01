import com.google.crypto.tink.KeysetHandle
import encryption.EncryptedGoal
import encryption.EncryptedSubGoal
import encryption.IGoalEncryptionService
import goals.Goal
import goals.GoalColor
import goals.ReachGoalWay
import goals.SubGoal
import internal.DataEncrypter
import internal.KeyEncrypter
import internal.KeyGenerator
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.Singles
import java.time.LocalDateTime

class GoalEncryptionService(
    private val keyGenerator: KeyGenerator,
    private val keyEncrypter: KeyEncrypter,
    private val subgoalEncryptionService: SubGoalEncryptionService
) : IGoalEncryptionService {

    override fun encrypt(goal: Goal): Single<EncryptedGoal> {
        val dataKey = keyGenerator.generateKeysetHandle()

        val encryptedSubGoals = encryptSubGoals(goal, dataKey)
        return Singles.zip(
            encryptGoalData(goal, dataKey),
            encryptedSubGoals
        ) { encryptedGoal, subgoals ->
            encryptedGoal.subgoals = subgoals
            encryptedGoal
        }
    }

    /**
     * Returns an encrypted version of the [Goal], only the data, not the subgoals.
     */
    private fun encryptGoalData(
        goal: Goal,
        dataKey: KeysetHandle
    ): Single<EncryptedGoal> {
        val encryptedDEK = keyEncrypter.encrypt(dataKey)
            .doOnSuccess {  }

        return encryptedDEK
            .flatMap { dek ->
                Single.just(with(DataEncrypter(dataKey)) {
                    EncryptedGoal(
                        dateTime = encrypt(goal.dateTime.toString()),
                        description = encrypt(goal.description),
                        uuid = goal.uuid,
                        isCompleted = encrypt(if (goal.isCompleted) "true" else "false"),
                        currentPositionAvatar = goal.currentPositionAvatar,
                        goalColor = goal.goalColor.name,
                        reachGoalWay = goal.chosenReachGoalWay.name,
                        encryptedDEK = dek
                    )
                })
            }
    }

    private fun encryptSubGoals(
        goal: Goal,
        dek: KeysetHandle
    ): Single<List<EncryptedSubGoal>> {
        return Observable.fromIterable(goal.subGoals.entries)
            .flatMapSingle {
                subgoalEncryptionService.encrypt(
                    it.component1(),
                    it.component2(),
                    dek
                )
            }
            .toList()
    }

    override fun decryptData(encryptedGoal: EncryptedGoal): Single<Goal> {
        return keyEncrypter
            .decrypt(encryptedGoal.encryptedDEK)
            .doOnSuccess {  }
            .flatMap { dek -> decryptGoalData(encryptedGoal, dek) }
    }

    private fun decryptGoalData(encryptedGoal: EncryptedGoal, dek: KeysetHandle): Single<Goal> {
        return decryptSubGoals(encryptedGoal, dek)
            .map { decryptedSubgoals ->
                with(DataEncrypter(dek)) {
                    val goal = Goal(
                        GoalColor.valueOf(encryptedGoal.goalColor),
                        encryptedGoal.uuid
                    )
                        .also {
                            it.dateTime = LocalDateTime.parse(decrypt(encryptedGoal.dateTime))
                            it.description = decrypt(encryptedGoal.description)
                            it.isCompleted = decrypt(encryptedGoal.isCompleted) == "true"
                            it.currentPositionAvatar = encryptedGoal.currentPositionAvatar
                            it.chosenReachGoalWay = ReachGoalWay.valueOf(encryptedGoal.reachGoalWay)
                        }
                    decryptedSubgoals.forEach { subgoalpair ->
                        goal.addSubGoal(
                            subgoalpair.first,
                            subgoalpair.second
                        )
                    }
                    goal
                }
            }
    }

    private fun decryptSubGoals(
        encryptedGoal: EncryptedGoal,
        dek: KeysetHandle
    ): Single<List<Pair<SubGoal, Int>>> {
        return Observable.fromIterable(encryptedGoal.subgoals)
            .flatMapSingle { subgoalEncryptionService.decrypt(it, dek) }.toList()
    }
}