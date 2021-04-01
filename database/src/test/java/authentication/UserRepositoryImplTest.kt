package authentication

import User
import factory.EntityFactory
import factory.UserFactory
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import org.junit.Test
import user.FirebaseUserDatabase
import user.UserEntity
import user.UserMapper
import user.UserRepository

class UserRepositoryImplTest {

    private val firebaseUserRepository = mockk<FirebaseUserDatabase>(relaxed = true)
    private val userMapper = mockk<UserMapper>()

    private val userRepository = UserRepository(
        userMapper,
        firebaseUserRepository
    )

    private val userWithoutEvents = UserFactory.makeUser(0)
    private val userEntity = EntityFactory.makeUserEntity()

    @Test
    fun userRepository_delete_authenticatedUser_Completes() {
        every { firebaseUserRepository.delete(userEntity) } returns Completable.complete()
        stubUserMapperToEntity(userWithoutEvents, userEntity)
        userRepository.delete(userWithoutEvents).test().assertComplete()
    }

    @Test
    fun userRepository_delete_nonAuthenticatedUser_Fails() {
        every { firebaseUserRepository.delete(userEntity) } returns Completable.error(RuntimeException("Unauthorized used."))
        stubUserMapperToEntity(userWithoutEvents, userEntity)
        userRepository.delete(userWithoutEvents).test().assertNotComplete()
    }

    @Test
    fun userRepository_insert_authenticatedUser_Completes() {
        every { firebaseUserRepository.initialiseUser(userEntity) } returns Completable.complete()
        stubUserMapperToEntity(userWithoutEvents, userEntity)
        userRepository.initialiseUser(userWithoutEvents).test().assertComplete()
    }

    @Test
    fun userRepository_insert_nonAuthenticatedUser_Fails() {
        every { firebaseUserRepository.initialiseUser(userEntity) } returns Completable.error(
            RuntimeException("Unauthorized used.")
        )
        stubUserMapperToEntity(userWithoutEvents, userEntity)
        userRepository.delete(userWithoutEvents).test().assertNotComplete()
    }

    @Test
    fun userRepository_get_nonAuthenticatedUser_errors() {
        every { firebaseUserRepository.get(userWithoutEvents.uuid) } returns Flowable.error(RuntimeException("Unauthorized used."))
        userRepository.get(userWithoutEvents.uuid)
            .test()
            .assertNoValues()
    }

    @Test
    fun userRepository_get_authenticatedUser_succeeds() {
        every { firebaseUserRepository.get(userEntity.uuid) } returns Flowable.just(
            userEntity
        )

        stubUserMapperFromEntity(userWithoutEvents, userEntity)

        userRepository.get(userWithoutEvents.uuid)
            .test()
            .assertValue { it.uuid == userWithoutEvents.uuid && it.events.size == 0 }
    }
/*
@Test
fun userRepository_getAll_returnsFlowable() {
     val userWithoutEvents2 = UserFactory.makeUser(0)
     val userEntity2 = EntityFactory.makeUserEntity()
    every { firebaseUserRepository.getAll() } returns Flowable.just(
        listOf(userEntity, userEntity2)
    )
    stubUsersMapperFromEntity(
        listOf(userWithoutEvents, userWithoutEvents2),
        listOf(userEntity, userEntity2)
    )
    userRepository.getAll()
        .test()
        .assertValue {
            it.size == 2
        }
}
*/
    private fun stubUserMapperFromEntity(model: User, entity: UserEntity) {
        every { userMapper.mapFromEntity(entity) } returns model
    }

    private fun stubUsersMapperFromEntity(models: List<User>, entities: List<UserEntity>) {
        every { userMapper.mapFromEntities(entities) } returns models
    }

    private fun stubUserMapperToEntity(model: User, entity: UserEntity) {
        every { userMapper.mapToEntity(model) } returns entity
    }
}