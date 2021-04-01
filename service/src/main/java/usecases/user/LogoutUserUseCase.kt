package usecases.user

import User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import repository.IAuthManager
import usecases.base.CompletableUseCase
import javax.servlet.http.HttpServletRequest

class LogoutUserUseCase(
    private val authManager: IAuthManager,
    observer: Scheduler
) : CompletableUseCase<LogoutUserUseCase.Params>(observer) {

    override fun buildUseCaseObservable(params: Params): Completable {
        return authManager.signOut(params.uuid)
    }

    data class Params(
        val uuid:String
    )
}
