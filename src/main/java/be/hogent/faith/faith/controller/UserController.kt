package be.hogent.faith.faith.controller

import User
import be.hogent.faith.faith.iservice.IUserService
import controllerModels.UserEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.EntityModel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/user")
class UserController @Autowired constructor(val userService: IUserService) {
    @GetMapping("/{uuid}")
    fun getUser(@PathVariable uuid:String): EntityModel<User> {
        val user = userService.getLoggedInUser(uuid)
        return EntityModel.of(user)
    }
    @PostMapping
    fun saveEvent(@RequestBody userEvent: UserEvent):ResponseEntity<*>{
        userService.saveEvent(userEvent.event,userEvent.user)
        val entity = EntityModel.of(userEvent.event)
        val location = ServletUriComponentsBuilder.fromCurrentRequest().path(
            "/user"
        ).buildAndExpand(userEvent.event).toUri()
        return ResponseEntity.created(location).body(entity)
    }
}