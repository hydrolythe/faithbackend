package encryption

import Event
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface IEventEncryptionService {
    /**
     * Encrypts the [event]s data and  files. Does the same  for the details in the event.
     *
     * @return an [EncryptedEvent] whose files are encrypted, and whose details are also encrypted.
     */
    fun encrypt(event: Event): Single<EncryptedEvent>

    /**
     * Decrypts the [encryptedEvent]s data, but not its files.
     */
    fun decryptData(encryptedEvent: EncryptedEvent): Single<Event>

    /**
     * Decrypts the files belonging to an event.
     * All files will be placed in the paths designated by the [StoragePathProvider].
     */
    fun decryptFiles(encryptedEvent: EncryptedEvent): Completable
}
