package usecases.detail.textdetail

import detail.TextDetail
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import repository.ITemporaryFileStorageRepository
import usecases.base.SingleUseCase

class LoadTextDetailUseCase(
    private val tempStorageRepo: ITemporaryFileStorageRepository,
    observer: Scheduler
) : SingleUseCase<String, LoadTextDetailUseCase.LoadTextParams>(observer) {

    override fun buildUseCaseSingle(params: LoadTextParams): Single<String> {
        return tempStorageRepo.loadTextFromExistingDetail(params.textDetail)
    }

    class LoadTextParams(
        val textDetail: TextDetail
    )
}