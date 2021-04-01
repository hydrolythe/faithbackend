package usecases.detailscontainer

import DetailsContainer
import User
import detail.Detail
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import repository.IDetailContainerRepository
import usecases.base.CompletableUseCase

class DeleteDetailsContainerDetailUseCase<T : DetailsContainer>(
    private val backpackRepository: IDetailContainerRepository<T>,
    observer: Scheduler
) : CompletableUseCase<DeleteDetailsContainerDetailUseCase.Params>(observer) {

    override fun buildUseCaseObservable(params: Params): Completable {
        return Completable.mergeArray(
            backpackRepository.deleteDetail(params.detail,params.uuid),
        ).concatWith(
            Completable.defer { Completable.fromAction { params.container.removeDetail(params.detail) } }
        )
    }

    data class Params(val uuid:String, val detail: Detail, val container: DetailsContainer)
}
