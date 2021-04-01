package common

import User
import factory.EntityFactory
import factory.UserFactory
import junit.framework.Assert.assertEquals
import org.junit.Test
import user.UserEntity
import user.UserMapper

class UserMapperTest {
    private val userMapper = UserMapper

    @Test
    fun `should map to User when UserEntity is given`() {
        val entity = EntityFactory.makeUserEntity()
        val model = userMapper.mapFromEntity(entity)
        assertEqualData(entity, model)
    }

    @Test
    fun `should map to UserEntity when User is given`() {
        val model = UserFactory.makeUser()
        val entity = userMapper.mapToEntity(model)
        assertEqualData(entity, model)
    }

    private fun assertEqualData(entity: UserEntity, model: User) {
        assertEquals(entity.uuid, model.uuid)
        assertEquals(entity.avatarName, model.avatarName)
        assertEquals(entity.username, model.username)
    }
}