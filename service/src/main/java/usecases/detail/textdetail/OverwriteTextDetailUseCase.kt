package usecases.detail.textdetail

import detail.TextDetail
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import repository.ITemporaryFileStorageRepository
import usecases.base.CompletableUseCase

/**
 * Use case to be used when the text inside an existing [TextDetail] should be overwritten
 * with text. This means the location of the file in the [TextDetail] is not changed.
 */
class OverwriteTextDetailUseCase(
    private val tempStorageRepo: ITemporaryFileStorageRepository,
    observer: Scheduler
) : CompletableUseCase<OverwriteTextDetailUseCase.Params>(observer) {

    override fun buildUseCaseObservable(params: Params): Completable {
        return tempStorageRepo.overwriteTextDetail(params.text, params.detail)
    }

    data class Params(
        val text: String,
        val detail: TextDetail
    )
}