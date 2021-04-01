package goal

import User
import encryption.EncryptedGoal
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import repository.IGoalRepository
import java.util.*

class GoalRepository(
    private val goalDatabase: GoalDatabase
) : IGoalRepository {

    private val goalMapper = GoalMapper

    /**
     * Gets the goal with the given [uuid] for the currently authenticated user.
     */
    override fun get(uuid: UUID, user:String): Flowable<EncryptedGoal> {
        return goalDatabase.get(user, uuid)
            .map(goalMapper::mapFromEntity)
    }

    /**
     * updates the goal for the currently authenticated user.
     */
    override fun update(encryptedGoal: EncryptedGoal, user: String): Completable {
        return Single.just(encryptedGoal)
            .map(goalMapper::mapToEntity)
            .flatMapCompletable { goal ->
                goalDatabase.update(goal, user)
            }
    }

    /**
     * Adds the goal for the authenticated user
     */
    override fun insert(encryptedGoal: EncryptedGoal, user: String): Completable {
        return Single.just(encryptedGoal)
            .map(goalMapper::mapToEntity)
            .flatMapCompletable { goal ->
                goalDatabase.insert(goal, user)
            }
    }

    /**
     * Deletes the goal with [goalUuid] for the currently authenticated user.
     */
    override fun delete(goalUuid: UUID, user: String): Completable {
        return goalDatabase.delete(goalUuid, user)
    }

    /**
     * Get all goals for the currently authenticated user.
     */
    override fun getAll(user: String): Flowable<List<EncryptedGoal>> {
        return goalDatabase.getAll(user)
            .map(goalMapper::mapFromEntities)
    }
}
