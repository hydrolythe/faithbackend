package be.hogent.faith.faith.service

import Event
import User
import be.hogent.faith.faith.iservice.IUserService
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import io.reactivex.rxjava3.subscribers.DisposableSubscriber
import usecases.event.SaveEventUseCase
import usecases.user.GetUserUseCase

class UserService(private val saveEventUseCase: SaveEventUseCase,
                  private val getUserUseCase: GetUserUseCase): IUserService {
    var user :User? = null
    override fun getLoggedInUser(uuid:String):User{
        getUserUseCase.execute(GetUserUseCase.Params(uuid), object : DisposableSubscriber<User>() {

            override fun onNext(t: User) {
                user = t
            }

            override fun onComplete() {

            }

            override fun onError(e: Throwable) {

            }
        })
        if(user==null){
            Thread.sleep(1000)
        }
        return user!!
    }
    override fun saveEvent(event: Event,user:User) {
        if (event.title.isNullOrEmpty()) {
            return
        }
        val params = SaveEventUseCase.Params(event,user)
        saveEventUseCase.execute(params, object : DisposableCompletableObserver() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {

            }
        })
    }
}