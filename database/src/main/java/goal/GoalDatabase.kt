package goal

import com.google.cloud.firestore.*
import com.google.firebase.auth.FirebaseAuth
import common.EVENTS_KEY
import common.GOALS_KEY
import common.USERS_KEY
import io.reactivex.rxjava3.core.*
import java.util.*


class GoalDatabase(
    private val fbAuth: FirebaseAuth,
    private val firestore: Firestore
) {

    /**
     * get goal with goalUid for the current authenticated user, also listens for changes
     */
    fun get(uuid: String, goalUuid: UUID): Flowable<EncryptedGoalEntity> {
        val currentUser = fbAuth.getUser(uuid)
            ?: return Flowable.error(RuntimeException("Unauthorized user."))
        return Flowable.create(FlowableOnSubscribe<DocumentSnapshot>() {
            @Override
            fun subscribe(emitter: FlowableEmitter<DocumentSnapshot>) {
                val registration: ListenerRegistration = firestore
                    .collection(USERS_KEY)
                    .document(currentUser.uid)
                    .collection(GOALS_KEY)
                    .document(goalUuid.toString())
                    .addSnapshotListener { documentSnapshot: DocumentSnapshot?, firestoreException: FirestoreException? ->
                        @Override
                        fun onEvent(documentSnapshot: DocumentSnapshot, e: FirestoreException) {
                            if (e != null && !emitter.isCancelled()) {
                                emitter.onError(e);
                                return;
                            }
                            emitter.onNext(documentSnapshot);
                        }
                    }
                emitter.setCancellable {
                    fun cancel() {
                        registration.remove()
                    }
                }
            }
        }, BackpressureStrategy.DROP).map { it.toObject(EncryptedGoalEntity::class.java) }
    }

    /**
     * get all goals for current authenticated user, also listens for changes
     */
    fun getAll(uuid: String): Flowable<List<EncryptedGoalEntity>> {
        val currentUser = fbAuth.getUser(uuid)
        if (currentUser == null) {
            return Flowable.error(java.lang.RuntimeException("Unauthorized user"))
        } else {
            return Flowable.create<QuerySnapshot>({
                @Override
                fun subscribe(emitter: FlowableEmitter<QuerySnapshot>) {
                    val registration = firestore
                        .collection(USERS_KEY)
                        .document(currentUser.uid)
                        .collection(GOALS_KEY)
                        .addSnapshotListener { _: QuerySnapshot?, _: FirestoreException? ->
                            @Override
                            fun onEvent(querySnapshot: QuerySnapshot, e: FirestoreException) {
                                if (e != null && !emitter.isCancelled) {
                                    emitter.onError(e)
                                    return
                                }
                                emitter.onNext(querySnapshot)
                            }
                        }
                    emitter.setCancellable {
                        @Override
                        fun cancel() {
                            registration.remove()
                        }
                    }
                }
            }, BackpressureStrategy.DROP).map {
                it.map { document -> document.toObject(EncryptedGoalEntity::class.java) }
            }
        }
    }

    /**
     * insert goals for current authenticated user
     */
    fun insert(item: EncryptedGoalEntity, uuid: String): Completable {
        val currentUser = fbAuth.getUser(uuid)
        return if (currentUser == null) {
            Completable.error(java.lang.RuntimeException("Unauthorized user"))
        } else {
            Completable.fromAction {
                firestore
                    .collection(USERS_KEY)
                    .document(currentUser.uid)
                    .collection(GOALS_KEY)
                    .document(item.uuid)
                    .set(item, SetOptions.merge())
            }
        }
    }

    /**
     * update goals for current authenticated user
     */
    fun update(item: EncryptedGoalEntity, uuid: String): Completable {
        // insert overwrites by default
        return insert(item, uuid)
    }

    /**
     * delete goal for current authenticated user
     */
    fun delete(goalUuid: UUID, uuid: String): Completable {
        val currentUser = fbAuth.getUser(uuid)
        return if (currentUser == null) {
            Completable.error(java.lang.RuntimeException("Unauthorized user"))
        } else {
            Completable.create {
                firestore
                    .collection(USERS_KEY)
                    .document(currentUser.uid)
                    .collection(GOALS_KEY)
                    .document(goalUuid.toString())
                    .delete()
            }
        }
    }
}