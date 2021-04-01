package usecases.user

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Scheduler
import repository.IAuthManager
import usecases.base.MaybeUseCase
import javax.servlet.http.HttpServletRequest

class LoginUserUseCase(
    private val authManager: IAuthManager,
    observer: Scheduler
) : MaybeUseCase<String?, LoginUserUseCase.Params>(observer) {

    override fun buildUseCaseMaybe(params: Params): Maybe<String?> {
        return authManager.signIn(params.uuid)
    }

    data class Params(
        val uuid:String
    )
}