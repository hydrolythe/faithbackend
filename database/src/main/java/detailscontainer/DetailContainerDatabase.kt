package detailscontainer

import com.google.cloud.firestore.*
import com.google.firebase.auth.FirebaseAuth
import common.CONTAINERS_KEY
import common.EncryptedDetailEntity
import common.USERS_KEY
import detail.Detail
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import java.lang.RuntimeException
import java.util.concurrent.Executor

abstract class DetailContainerDatabase<DetailContainer>(
    private val fbAuth: FirebaseAuth,
    private val firestore: Firestore
) {
    abstract val containerName: String

    fun insertDetail(item: EncryptedDetailEntity, uuid: String): Completable {
        val currentUser = fbAuth.getUser(uuid)
        return if (currentUser == null) {
            Completable.error(RuntimeException("Unauthorized user"))
        } else {
            Completable.fromAction {
                firestore
                    .collection(USERS_KEY)
                    .document(currentUser.uid)
                    .collection(containerName)
                    .document(item.uuid)
                    .set(item, SetOptions.merge())
            }
        }
    }

    fun getAll(uuid: String): Flowable<List<EncryptedDetailEntity>> {
        val currentUser = fbAuth.getUser(uuid)
        if (currentUser == null) {
            return Flowable.error(RuntimeException("Unauthorized user"))
        } else {
            return Flowable.create<QuerySnapshot>({
                it.onNext(firestore
                    .collection(USERS_KEY)
                    .document(currentUser.uid)
                    .collection(containerName)
                    .get()
                    .get())
                it.onComplete()
            }, BackpressureStrategy.DROP).map {
                it.map { document -> document.toObject(EncryptedDetailEntity::class.java) }
            }
        }
    }

    fun delete(item: Detail, uuid: String): Completable {
        val currentUser = fbAuth.getUser(uuid)
        return if (currentUser == null) {
            Completable.error(RuntimeException("Unauthorized user"))
        } else {
            Completable.fromAction {
                firestore
                    .collection(USERS_KEY)
                    .document(currentUser.uid)
                    .collection(containerName)
                    .document(item.uuid.toString())
                    .delete()
            }
        }
    }

    fun insertContainer(item: EncryptedDetailsContainerEntity, uuid: String): Completable {
        val currentUser = fbAuth.getUser(uuid)
        return if (currentUser == null) {
            Completable.error(RuntimeException("Unauthorized user"))
        } else {
            Completable.fromAction {
                firestore
                    .collection(USERS_KEY)
                    .document(currentUser.uid)
                    .collection(CONTAINERS_KEY)
                    .document(containerName)
                    .set(item, SetOptions.merge())
            }
        }
    }

    fun getContainer(uuid: String): Maybe<EncryptedDetailsContainerEntity> {
        val currentUser = fbAuth.getUser(uuid)
        if (currentUser == null) {
            return Maybe.error(RuntimeException("Unauthorized user"))
        } else {
            return Maybe.create<DocumentSnapshot> { emitter ->
                emitter.onSuccess(
                    firestore
                    .collection(USERS_KEY)
                    .document(currentUser.uid)
                    .collection(CONTAINERS_KEY)
                    .document(containerName)
                    .get()
                    .get()
                )
                }.map {
                    it.toObject(EncryptedDetailsContainerEntity::class.java)
            }
        }
    }
}