package be.hogent.faith.faith.controller

import Event
import User
import be.hogent.faith.faith.SecurityService
import be.hogent.faith.faith.iservice.IEventListService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.EntityModel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/eventlist")
class EventListController @Autowired constructor(var eventListService: IEventListService, var securityService: SecurityService) {
    @GetMapping
    fun loadEvents(user:User): EntityModel<List<Event>> {
        val events = eventListService.loadEvents(user)
        return EntityModel.of(events)
    }

    @DeleteMapping
    fun deleteEvents(event:Event,user:User): ResponseEntity<*>{
        eventListService.deleteEvent(event,user)
        return ResponseEntity.noContent().build<Event>()
    }
}