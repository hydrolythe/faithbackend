package be.hogent.faith.faith.service

import be.hogent.faith.faith.iservice.IWelcomeService
import io.reactivex.rxjava3.observers.DisposableMaybeObserver
import org.springframework.stereotype.Service
import usecases.user.LoginUserUseCase
import javax.servlet.http.HttpServletRequest

class WelcomeService(private val loginUserUseCase: LoginUserUseCase):IWelcomeService{

    private var token: String? = null
    /**
     * logs in a user
     */
    override fun login(uuid:String):String {
        val params = LoginUserUseCase.Params(uuid)
        loginUserUseCase.execute(params, LoginUserUseCaseHandler())
        while(token.isNullOrEmpty()){
            Thread.sleep(1000)
        }
        return token!!
    }

    private inner class LoginUserUseCaseHandler : DisposableMaybeObserver<String?>() {
        // returns uuid when successfully logged in
        override fun onSuccess(t: String?) {
           token = t
        }

        override fun onComplete() {
            token = "complete"
        }

        override fun onError(e: Throwable) {
            token = e.localizedMessage
        }
    }
}