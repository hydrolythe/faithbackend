package usecases.detail.textdetail

import detail.TextDetail
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import repository.ITemporaryFileStorageRepository
import usecases.base.SingleUseCase

class CreateTextDetailUseCase(
    private val tempStorageRepo: ITemporaryFileStorageRepository,
    observer: Scheduler
) : SingleUseCase<TextDetail, CreateTextDetailUseCase.Params>(observer) {

    override fun buildUseCaseSingle(params: Params): Single<TextDetail> {
        return tempStorageRepo.storeText(params.text)
            .map { saveFile -> TextDetail(saveFile) }
    }

    class Params(
        val text: String
    )
}
