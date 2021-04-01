package be.hogent.faith.faith.service

import User
import be.hogent.faith.faith.iservice.IRegisterAvatarService
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import usecases.user.InitialiseUserUseCase

class RegisterAvatarService(
    private val initialiseUserUseCase: InitialiseUserUseCase
):IRegisterAvatarService {

    override fun initialiseUser(user: User) {
        val params = InitialiseUserUseCase.Params(user)
        initialiseUserUseCase.execute(params, object : DisposableCompletableObserver() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                print(e)
            }
        })
    }
}