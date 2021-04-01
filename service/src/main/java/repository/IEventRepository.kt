package repository

import Event
import User
import encryption.EncryptedEvent
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import java.util.*

interface IEventRepository {

    fun update(encryptedEvent: EncryptedEvent,user:String): Completable

    /**
     * Saves the given [event] to the events of the given [user]
     */
    fun insert(encryptedEvent: EncryptedEvent,user:String): Completable

    fun get(uuid: UUID,user:String): Flowable<EncryptedEvent>

    /**
     * Deletes the  given [event]. The [user] is required to check that the event belongs to
     * the currently logged in user.
     */
    fun delete(event: Event, user: String): Completable

    /**
     * Returns the data of all events associated with the currently logged-in user.
     * This does no ensure that the files for these events are available!
     * Use the [be.hogent.faith.service.usecases.event.MakeEventFilesAvailableUseCase] to ensure this.
     */
    fun getAll(uuid:String): Flowable<List<EncryptedEvent>>
}