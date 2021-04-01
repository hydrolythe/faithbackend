package usecases.event

import Event
import detail.Detail
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import usecases.base.CompletableUseCase

class DeleteEventDetailUseCase(
    observer: Scheduler
) : CompletableUseCase<DeleteEventDetailUseCase.Params>(observer) {

    override fun buildUseCaseObservable(params: Params): Completable {
        return Completable.fromAction {
            with(params) {
                if (detail.file.exists()) {
                    detail.file.delete()
                }
                event.removeDetail(detail)
            }
        }
    }

    data class Params(
        val detail: Detail,
        val event: Event
    )
}
