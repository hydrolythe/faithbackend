package event

import Event
import User
import com.google.cloud.firestore.*
import com.google.firebase.auth.FirebaseAuth
import common.EVENTS_KEY
import common.GOALS_KEY
import common.USERS_KEY
import goal.EncryptedGoalEntity
import io.reactivex.rxjava3.core.*
import java.util.*

/**
 * document hierarchy in Firestore : users/{userUid}/events/{eventUuid}.
 * storage hierarchy in Firestorage : idem
 * the ruleset on both storages is so that a user can only CRUD his documents
 */
class EventDatabase(
    private val fbAuth: FirebaseAuth,
    private val firestore: Firestore
) {

    /**
     * get event with eventUid for the current authenticated user, also listens for changes
     */
    fun get(eventUuid: UUID, uuid: String): Flowable<EncryptedEventEntity> {
        val currentUser = fbAuth.getUser(uuid)
            ?: return Flowable.error(RuntimeException("Unauthorized user."))
        return Flowable.create(FlowableOnSubscribe<DocumentSnapshot>() {
            it.onNext(firestore
                .collection(USERS_KEY)
                .document(currentUser.uid)
                .collection(EVENTS_KEY)
                .document(eventUuid.toString())
                .get()
                .get()
            )
            it.onComplete()
        }, BackpressureStrategy.DROP).map {
            it.toObject(EncryptedEventEntity::class.java)
        }

    }

    /**
     * get all events for current authenticated user, also listens for changes
     */
    fun getAll(uuid: String): Flowable<List<EncryptedEventEntity>> {
        val currentUser = fbAuth.getUser(uuid)
        if (currentUser == null) {
            return Flowable.error(java.lang.RuntimeException("Unauthorized user"))
        } else {
            val document = firestore.collection(USERS_KEY).document(currentUser.uid).get().get()
            if(document.exists()){
            return Flowable.create<QuerySnapshot>({
                it.onNext(firestore
                    .collection(USERS_KEY)
                    .document(currentUser.uid)
                    .collection(EVENTS_KEY)
                    .get()
                    .get())
                it.onComplete()
            }, BackpressureStrategy.DROP).map {
                it.map { document -> document.toObject(EncryptedEventEntity::class.java) }
            }
            }
            else{
                return Flowable.create({
                    it.onNext(listOf())
                    it.onComplete()
                },BackpressureStrategy.DROP)
            }
        }
    }

    fun insert(item: EncryptedEventEntity, uuid: String): Completable {
        val currentUser = fbAuth.getUser(uuid)
        return if (currentUser == null) {
            Completable.error(java.lang.RuntimeException("Unauthorized user"))
        } else {
            Completable.fromAction {
                firestore
                    .collection(USERS_KEY)
                    .document(currentUser.uid)
                    .collection(EVENTS_KEY)
                    .document(item.uuid)
                    .set(item, SetOptions.merge())
            }
        }
    }

    fun delete(event: Event, uuid: String): Completable {
        val currentUser = fbAuth.getUser(uuid)
        return if (currentUser == null) {
            Completable.error(java.lang.RuntimeException("Unauthorized user"))
        } else {
            Completable.fromAction {
                firestore
                    .collection(USERS_KEY)
                    .document(currentUser.uid)
                    .collection(EVENTS_KEY)
                    .document(event.uuid.toString())
                    .delete()
            }
        }
    }

    fun update(item: EncryptedEventEntity, uuid: String): Completable {
        // insert overwrites by default
        return insert(item, uuid)
    }
}