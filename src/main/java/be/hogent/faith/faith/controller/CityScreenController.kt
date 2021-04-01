package be.hogent.faith.faith.controller

import User
import be.hogent.faith.faith.CookieService
import be.hogent.faith.faith.SecurityService
import be.hogent.faith.faith.iservice.ICityScreenService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/city")
class CityScreenController @Autowired constructor(var cityScreenService: ICityScreenService, var cookieUtils:CookieService) {
    @PostMapping("/{uuid}")
    fun logout(@PathVariable uuid:String): ResponseEntity<*> {
        cityScreenService.logout(uuid)
        cookieUtils.deleteSecureCookie("session");
        cookieUtils.deleteCookie("authenticated");
        return ResponseEntity.ok().body<String>(uuid)
    }
}