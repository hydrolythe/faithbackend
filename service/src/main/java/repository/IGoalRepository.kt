package repository

import User
import encryption.EncryptedGoal
import goals.Goal
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import java.util.*

interface IGoalRepository {
    /**
     * Updates the given [Goal]  for the authenticated user
     */
    fun update(goal: EncryptedGoal,user:String): Completable

    /**
     * Adds the given [Goal] to the goals of the authenticated user
     */
    fun insert(goal: EncryptedGoal,user:String): Completable

    fun get(uuid: UUID,user:String): Flowable<EncryptedGoal>

    /**
     * Deletes the goal with given [goalUuid] for the currently authenticated user.
     */
    fun delete(goalUuid: UUID,user:String): Completable

    /**
     * Returns all goals  associated with the currently authenticated user.
     */
    fun getAll(user:String): Flowable<List<EncryptedGoal>>
}