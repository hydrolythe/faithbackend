package repository

import User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface IUserRepository {

    fun delete(user: User): Completable

    fun initialiseUser(user: User): Completable

    fun get(uid: String): Flowable<User>
}