package event

import Event
import User
import encryption.EncryptedEvent
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import repository.IEventRepository
import java.util.*

open class EventRepository(
    private val eventDatabase: EventDatabase
) : IEventRepository {

    private val eventMapper = EventMapper

    /**
     * Gets the event with the given [uuid] for the currently authenticated user.
     * Does not download the files belonging to the event!
     */
    override fun get(uuid: UUID, user: String): Flowable<EncryptedEvent> {
        return eventDatabase.get(uuid, user)
            .map(eventMapper::mapFromEntity)
    }

    override fun update(encryptedEvent: EncryptedEvent, user: String): Completable {
        return Single.just(encryptedEvent)
            .map(eventMapper::mapToEntity)
            .flatMapCompletable { event ->
                eventDatabase.update(event, user)
            }
    }

    /**
     * Adds an event for the authenticated user together with its details
     * @return a [Maybe<Event>] that only succeeds when both the event and its details are inserted successfully.
     */
    override fun insert(encryptedEvent: EncryptedEvent, user: String): Completable {
        return Single.just(encryptedEvent)
            .map(eventMapper::mapToEntity)
            .flatMapCompletable { event ->
                eventDatabase.insert(event, user)
            }
    }

    override fun delete(event: Event, user: String): Completable {
        return eventDatabase.delete(event, user)
    }

    override fun getAll(uuid: String): Flowable<List<EncryptedEvent>> {
        return eventDatabase.getAll(uuid)
            .map(eventMapper::mapFromEntities)
    }
}