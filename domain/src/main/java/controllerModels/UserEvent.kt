package controllerModels

import Event
import User

data class UserEvent(val user:User,val event:Event)
