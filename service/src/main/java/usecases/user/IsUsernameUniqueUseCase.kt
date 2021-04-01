package usecases.user

import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import repository.IAuthManager
import usecases.base.SingleUseCase

class IsUsernameUniqueUseCase(
    private val authManager: IAuthManager,
    observer: Scheduler
) : SingleUseCase<Boolean, IsUsernameUniqueUseCase.Params>(observer) {

    override fun buildUseCaseSingle(params: Params): Single<Boolean> {
        if (params.username.isBlank())
            return Single.error(RuntimeException("username moet ingevuld zijn"))
        return authManager.isUsernameUnique(params.username + "@faith.be")
    }

    data class Params(
        val username: String
    )
}