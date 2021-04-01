package user

import User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import repository.IUserRepository

open class UserRepository(
    private val userMapper: UserMapper,
    private val userDatabase: FirebaseUserDatabase
) : IUserRepository {

    /**
     * deletes the user. item must be the authenticated user
     */
    override fun delete(user: User): Completable {
        return userDatabase.delete(userMapper.mapToEntity(user))
    }

    /**
     * Initialize a user.
     */
    override fun initialiseUser(user: User): Completable {
        return userDatabase.initialiseUser(userMapper.mapToEntity(user))
    }

    /**
     * gets the current user. This must be the uid of the authenticated user
     */
    override fun get(uid: String): Flowable<User> {
        return userDatabase.get(uid).map { userMapper.mapFromEntity(it) }
    }
}