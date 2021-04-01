package usecases.detailscontainer

import DetailsContainer
import User
import detail.Detail
import encryption.IDetailContainerEncryptionService
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import repository.IDetailContainerRepository
import usecases.base.CompletableUseCase

class SaveDetailsContainerDetailUseCase<Container : DetailsContainer>(
    private val detailContainerRepository: IDetailContainerRepository<Container>,
    private val detailContainerEncryptionService: IDetailContainerEncryptionService<Container>,
    observer: Scheduler,
    subscriber: Scheduler = Schedulers.io()
) : CompletableUseCase<SaveDetailsContainerDetailUseCase.Params>(observer, subscriber) {

    override fun buildUseCaseObservable(params: Params): Completable {
        return detailContainerRepository.getEncryptedContainer(params.uuid)
            .subscribeOn(subscriber)
            .doOnSuccess {  }
            .doOnError {
                println(it)
            }
            .flatMap { container ->
                detailContainerEncryptionService.encrypt(params.detail, container)
                    .subscribeOn(subscriber)
                    .doOnSuccess {  }
                    .doOnError {
                        println(it)
                    }
            }
            .flatMapCompletable { savedEncryptedDetail ->
                detailContainerRepository.insertDetail(savedEncryptedDetail, params.uuid)
                    .subscribeOn(subscriber)
                    .doOnComplete {  }
                    .doOnError {
                        it.printStackTrace()
                    }
            }
        // We don't have to add the detail to the container ourselves because we updated in the
        // database and updates to the container are automatically send to whoever is listening to
        // the GetDetailsContainerDataUseCase.
    }

    data class Params(val uuid:String, val detailsContainer: DetailsContainer, val detail: Detail)
}
