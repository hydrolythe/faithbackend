package usecases.event

import Event
import detail.Detail
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import repository.ITemporaryFileStorageRepository
import usecases.base.CompletableUseCase

class SaveEventDetailUseCase(
    private val tempStorageRepo: ITemporaryFileStorageRepository,
    observer: Scheduler
) : CompletableUseCase<SaveEventDetailUseCase.Params>(observer) {

    override fun buildUseCaseObservable(params: Params): Completable {
        if (params.event.details.contains(params.detail)) {
            return Completable.complete()
        }
        return tempStorageRepo.storeDetailWithEvent(
            params.detail,
            params.event
        ).doOnComplete {
            params.event.addDetail(params.detail)
        }
    }

    data class Params(
        val detail: Detail,
        val event: Event
    )
}