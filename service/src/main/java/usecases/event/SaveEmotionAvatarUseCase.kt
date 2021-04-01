package usecases.event

import Event
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import repository.ITemporaryFileStorageRepository
import usecases.base.CompletableUseCase
import java.awt.image.BufferedImage

/*
**
* Stores the (colored) emotion avatarName for an event on the device's storage.
* Will overwrite a previously stored avatarName.
*/
class SaveEmotionAvatarUseCase(
    private val tempStorageRepo: ITemporaryFileStorageRepository,
    observer: Scheduler
) : CompletableUseCase<SaveEmotionAvatarUseCase.Params>(
    observer
) {
    override fun buildUseCaseObservable(params: Params): Completable {
        return Completable.fromSingle(
            tempStorageRepo.storeBitmap(
                params.bitmap
            ).doOnSuccess {
                params.event.emotionAvatar = it
            }
        )
    }

    data class Params(val bitmap: BufferedImage, val event: Event)
}