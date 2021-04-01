package user

import com.google.cloud.firestore.*
import com.google.firebase.auth.FirebaseAuth
import common.CONTAINERS_KEY
import common.GOALS_KEY
import common.USERS_KEY
import goal.EncryptedGoalEntity
import io.reactivex.rxjava3.core.*

/**
 *  The ruleset in Firestorage are set so a user can only CRUD it's own documents
 *  currentUser is the user who is logged in
 */
class FirebaseUserDatabase(
    private val fbAuth: FirebaseAuth,
    private val firestore: Firestore
) : IUserDatabase {
    /**
     * get User for currentUser, observe changes
     */
    override fun get(uid: String): Flowable<UserEntity> {
        val currentUser = fbAuth.getUser(uid) ?:
        return Flowable.error(RuntimeException("Unauthorized user."))
        val document = firestore.collection(USERS_KEY).document(currentUser.uid).get().get()
        return if(document.exists()){
            Flowable.create(FlowableOnSubscribe<DocumentSnapshot> {
                it.onNext(
                    firestore.collection(USERS_KEY).document(currentUser.uid).get().get())
                it.onComplete()
            }, BackpressureStrategy.DROP).map {
                it.toObject(UserEntity::class.java)
            }
        } else{
            Flowable.create({
                it.onNext(UserEntity(uid,currentUser.email,""))
                it.onComplete()
            }, BackpressureStrategy.DROP)
        }
    }

    /**
     * Set user in Firestorage in users/uuid
     */
    override fun initialiseUser(item: UserEntity): Completable {
        val document = firestore.collection(USERS_KEY).document(item.uuid)
        return Completable.fromAction {
            document.set(item, SetOptions.merge())
        }
    }

    /**
     * delete the user corresponding the currentUser in firestore
     */
    override fun delete(item: UserEntity): Completable {
        val currentUser = fbAuth.getUser(item.uuid) ?: return Completable.error(RuntimeException("Unauthorized user."))
        return Completable.fromAction {
            firestore.collection(USERS_KEY).document(currentUser.uid).delete()
        }
    }
}