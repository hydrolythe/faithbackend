package be.hogent.faith.faith.iservice

import Event
import User

interface IEventListService {
    fun loadEvents(user:User):List<Event>
    fun deleteEvent(event:Event,user:User)
}