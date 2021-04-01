package be.hogent.faith.faith.controller

import User
import be.hogent.faith.faith.iservice.IRegisterAvatarService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/register")
class RegisterAvatarController @Autowired constructor(val registerAvatarService:IRegisterAvatarService) {
    @PostMapping
    fun createUser(@RequestBody user:User): ResponseEntity<*> {
        registerAvatarService.initialiseUser(user)
        val location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(user).toUri()
        return ResponseEntity.created(location).body(user)
    }
}