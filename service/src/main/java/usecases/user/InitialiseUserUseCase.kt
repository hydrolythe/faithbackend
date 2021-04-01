package usecases.user

import Backpack
import Cinema
import TreasureChest
import User
import encryption.ContainerType
import encryption.IDetailContainerEncryptionService
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import repository.IDetailContainerRepository
import repository.IUserRepository
import usecases.base.CompletableUseCase

class InitialiseUserUseCase(
    private val userRepository: IUserRepository,
    private val backpackRepository: IDetailContainerRepository<Backpack>,
    private val cinemaRepository: IDetailContainerRepository<Cinema>,
    private val treasureChestRepository: IDetailContainerRepository<TreasureChest>,
    private val backpackEncryptionService: IDetailContainerEncryptionService<Backpack>,
    private val cinemaEncryptionService: IDetailContainerEncryptionService<Cinema>,
    private val treasureChestEncryptionService: IDetailContainerEncryptionService<TreasureChest>,
    observer: Scheduler,
    subscriber: Scheduler = Schedulers.io()
) : CompletableUseCase<InitialiseUserUseCase.Params>(observer, subscriber) {

    override fun buildUseCaseObservable(params: Params): Completable {
        val createUser = userRepository
            .initialiseUser(params.user)
            .doOnComplete {
                println("User werd aangemaakt.")
            }
            .doOnError {
                println(it)
            }

        val createBackpack = backpackEncryptionService.createContainer(ContainerType.BACKPACK)
            .subscribeOn(subscriber)
            .doOnSuccess {
                println("Backpack werd aangemaakt.")
            }
            .doOnError {
                println(it)
            }
            .flatMapCompletable{
                    container -> backpackRepository.saveEncryptedContainer(container,params.user.uuid)}
            .doOnComplete {  }
            .doOnError {  }

        val createCinema = cinemaEncryptionService.createContainer(ContainerType.CINEMA)
            .subscribeOn(subscriber)
            .doOnSuccess {
                println("Cinema werd aangemaakt")
            }
            .doOnError {
                println(it)
            }
            .flatMapCompletable{container -> cinemaRepository.saveEncryptedContainer(container,params.user.uuid)}
            .doOnComplete {  }
            .doOnError {
                println(it)
            }

        val createTreasureChest =
            treasureChestEncryptionService.createContainer(ContainerType.TREASURECHEST)
                .subscribeOn(subscriber)
                .doOnSuccess {
                    println("Schatkist werd aangemaakt.")
                }
                .doOnError {  }
                .flatMapCompletable{container -> treasureChestRepository.saveEncryptedContainer(container,params.user.uuid)}
                .doOnComplete {  }
                .doOnError {
                    println(it)
                }

        return createUser
            .andThen(
                Completable.defer { createCinema }
            )
            .andThen(
                Completable.defer { createTreasureChest }
            )
            .andThen(
                Completable.defer { createBackpack }
            )
    }

    data class Params(
        val user: User
    )
}