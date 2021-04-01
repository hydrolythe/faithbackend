package usecases.detailscontainer

import DetailsContainer
import User
import detail.Detail
import encryption.EncryptedDetailsContainer
import encryption.IDetailContainerEncryptionService
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import repository.IDetailContainerRepository
import usecases.base.CompletableUseCase
import usecases.base.ObservableUseCase
import usecases.base.SingleUseCase

class LoadDetailFileUseCase<Container : DetailsContainer>(
    private val containerRepository: IDetailContainerRepository<Container>,
    private val detailContainerEncryptionService: IDetailContainerEncryptionService<Container>,
    observer: Scheduler
) : CompletableUseCase<LoadDetailFileUseCase.Params>(observer) {

    override fun buildUseCaseObservable(params: Params): Completable {

            return(containerRepository.getEncryptedContainer(params.uuid))
                .doOnError {  }
                .flatMapCompletable { encryptedContainer ->
                    detailContainerEncryptionService.decryptFile(params.detail, encryptedContainer)
                        .subscribeOn(subscriber)
                        .doOnError {  }
                }
    }

    class Params(
        val uuid:String,
        val detail: Detail
    )
}
