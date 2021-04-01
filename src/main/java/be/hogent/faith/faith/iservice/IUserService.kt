package be.hogent.faith.faith.iservice

import Event
import User

interface IUserService {
    fun getLoggedInUser(uuid:String):User
    fun saveEvent(event:Event,user:User)
}