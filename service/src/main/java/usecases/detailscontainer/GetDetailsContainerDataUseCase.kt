package usecases.detailscontainer

import DetailsContainer
import User
import detail.Detail
import encryption.IDetailContainerEncryptionService
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import repository.IDetailContainerRepository
import usecases.base.ObservableUseCase

class GetDetailsContainerDataUseCase<T : DetailsContainer>(
    private val detailsContainerRepository: IDetailContainerRepository<T>,
    private val detailContainerEncryptionService: IDetailContainerEncryptionService<T>,
    observer: Scheduler,
    subscriber: Scheduler = Schedulers.io()
) : ObservableUseCase<List<Detail>, GetDetailsContainerDataUseCase.Params>(observer, subscriber) {

    override fun buildUseCaseObservable(params: Params): Observable<List<Detail>> {
        return detailsContainerRepository.getEncryptedContainer(params.uuid)
            .subscribeOn(subscriber)
            .doOnSuccess {  }
            .doOnError {
                it.printStackTrace()
            }
            .flatMapObservable { container ->
                detailsContainerRepository.getAll(params.uuid)
                    .subscribeOn(subscriber)
                    .doOnNext {  }
                    .doOnError {
                        it.printStackTrace()
                    }
                    .toObservable()
                    .concatMapSingle { list ->
                        Observable.fromIterable(list)
                            .flatMapSingle {
                                detailContainerEncryptionService.decryptData(it, container)
                                    .subscribeOn(subscriber)
                                    .doOnSuccess {  }
                                    .doOnError {
                                        it.printStackTrace()
                                    }
                            }.toList()
                    }
            }
    }

    class Params(val uuid: String)
}