package be.hogent.faith.faith.service

import User
import be.hogent.faith.faith.iservice.ICityScreenService
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import org.springframework.stereotype.Service
import usecases.user.LogoutUserUseCase

class CityScreenService(private val logoutUserUseCase: LogoutUserUseCase): ICityScreenService
{
    override fun logout(uuid:String) {
        logoutUserUseCase.execute(LogoutUserUseCase.Params(uuid), LogoutUserUseCaseHandler())
    }

    private inner class LogoutUserUseCaseHandler : DisposableCompletableObserver() {
        override fun onComplete() {

        }

        override fun onError(e: Throwable) {

        }
    }

    override fun onCleared() {
        logoutUserUseCase.dispose()
    }
}